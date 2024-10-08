package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.dto.search.AdminSearchingRequest;
import ir.moslehi.finalprojectphase4.email.EmailSender;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.exception.NotValidInput;
import ir.moslehi.finalprojectphase4.model.Customer;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.enums.Role;
import ir.moslehi.finalprojectphase4.model.ConfirmationToken;
import ir.moslehi.finalprojectphase4.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final ExpertService expertService;
    private final PersonService personService;

    @PersistenceContext
    private EntityManager entityManager;

    public void sava(Customer customer) {
        Date now = new Date();
        personService.findByEmail(customer.getEmail());
        customer.setValidity(0L);
        customer.setDateOfSignUp(now);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.ROLE_CUSTOMER);
        customerRepository.save(customer);
    }

    @Transactional
    public String confirmToken(String token) {
        customerRepository.enableCustomer(expertService.checkConfirmToken(token).getCustomer().getEmail());
        return "confirmed";
    }

    public Customer register(Customer customer) {

        sava(customer);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .customer(customer)
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/api/customer/registration/confirm?token=" + token;
        emailSender.send(customer.getEmail(), expertService.buildEmail(customer.getFirstname(), link));

        return customer;
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("customer with email " + email + " wasn't found")
        );
    }

    public Customer update(Customer customer, String email) {
        Customer foundeCustomer = findByEmail(email);
        foundeCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
        personService.findByEmail(customer.getEmail());
        foundeCustomer.setEmail(customer.getEmail());
        return customerRepository.save(foundeCustomer);
    }


    public void updateValidity(Customer customer, Long price) {
        long validity = customer.getValidity() - price;
        if (validity < 0) {
            throw new NotValidInput("you don't have enough validity");
        }
        customer.setValidity(validity);
        customerRepository.save(customer);
    }

    public List<Customer> customerSearch(AdminSearchingRequest adminSearchingRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> customerQuery = builder.createQuery(Customer.class);
        Root<Customer> root = customerQuery.from(Customer.class);
        List<Predicate> predicates = new ArrayList<>();

        if (adminSearchingRequest.userEnabled() != null)
            predicates.add(builder.equal(root.get("enabled"), adminSearchingRequest.userEnabled()));

        expertService.personsInfoInSearching
                (adminSearchingRequest, builder, root.get("role"), root.get("firstname"),
                        root.get("lastname"), root.get("email"), root.get("validity")
                , root.get("dateOfSignUp"), predicates);

        customerQuery.where(builder.and(predicates.toArray(predicates.toArray(new Predicate[]{}))));

        List<Customer> customerList = entityManager.createQuery(customerQuery).getResultList();
        List<Customer> newList = new ArrayList<>();

        if (adminSearchingRequest.orderNumForUser() != null) {
            customerList.retainAll(orderNumInSearching(adminSearchingRequest, builder, customerList, newList));
        }

        if (adminSearchingRequest.orderNumInDoneForUser() != null) {
            customerList.retainAll(orderNumInDoneInSearching(adminSearchingRequest, builder, customerList, newList));
        }

        return customerList;
    }

    private List<Customer> orderNumInSearching(AdminSearchingRequest adminSearchingRequest, CriteriaBuilder builder,
                                               List<Customer> customerList, List<Customer> newList) {
        CriteriaQuery<Object[]> orderQuery = builder.createQuery(Object[].class);
        Root<Orders> rootOrder = orderQuery.from(Orders.class);
        orderQuery.multiselect(
                rootOrder.get("customer"),
                builder.count(rootOrder)
        );

        orderQuery.groupBy(rootOrder.get("customer"));
        orderQuery.having(builder.gt(builder.count(rootOrder), adminSearchingRequest.orderNumForUser()));
        return getCustomers(customerList, newList, orderQuery);
    }

    private List<Customer> orderNumInDoneInSearching(AdminSearchingRequest adminSearchingRequest, CriteriaBuilder builder,
                                                     List<Customer> customerList, List<Customer> newList) {
        CriteriaQuery<Object[]> orderQuery = builder.createQuery(Object[].class);
        Root<Orders> rootOrder = orderQuery.from(Orders.class);
        Predicate orderStatusPredicate = builder.equal(rootOrder.get("orderStatus"), OrderStatus.DONE);
        orderQuery.multiselect(
                rootOrder.get("customer"),
                builder.count(rootOrder)
        );
        orderQuery.where(orderStatusPredicate);
        orderQuery.groupBy(rootOrder.get("customer"));
        orderQuery.having(builder.gt(builder.count(rootOrder), adminSearchingRequest.orderNumInDoneForUser()));
        return getCustomers(customerList, newList, orderQuery);
    }

    private List<Customer> getCustomers(List<Customer> customerList,
                                        List<Customer> newList, CriteriaQuery<Object[]> orderQuery) {
        List<Object[]> results = entityManager.createQuery(orderQuery).getResultList();
        for (Object[] result : results) {
            Customer customer = (Customer) result[0];
            newList.add(customer);
        }
        customerList.retainAll(newList);
        return customerList;
    }

    public List<Orders> ordersHistoryCustomer(String email, String orderStatus) {
        Arrays.stream(OrderStatus.values()).anyMatch((t) -> t.name().equals(orderStatus));
        Customer customer = findByEmail(email);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> orderCriteriaQuery = builder.createQuery(Orders.class);
        Root<Orders> root = orderCriteriaQuery.from(Orders.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("customer"), customer));
        predicates.add(builder.equal(root.get("orderStatus"), orderStatus));
        orderCriteriaQuery.where(builder.and(predicates.toArray(predicates.toArray(new Predicate[]{}))));
        return entityManager.createQuery(orderCriteriaQuery).getResultList();
    }

}
