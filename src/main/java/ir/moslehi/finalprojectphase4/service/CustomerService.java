package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.dto.search.UserSearchRequest;
import ir.moslehi.finalprojectphase4.email.EmailSender;
import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
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

    @PersistenceContext
    private EntityManager entityManager;

    public void sava(Customer customer) {
        Date now = new Date();
        if (customerRepository.findByEmail(customer.getEmail()).isPresent())
            throw new DuplicateInformationException(customer.getEmail() + " is duplicate");
        customer.setValidity(0L);
        customer.setDateOfSignUp(now);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.ROLE_CUSTOMER);
        customerRepository.save(customer);
    }

    @Transactional
    public String confirmToken(String token) {
        customerRepository.enableCustomer(expertService.checkConfirmToken(token).getPerson().getEmail());
        return "confirmed";
    }

    public Customer register(Customer customer) {

        sava(customer);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                customer
        );

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

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("customer with id " + id + " wasn't found")
        );
    }

    public Customer update(Customer customer,String email) {
        Customer foundeCustomer = findByEmail(email);
        foundeCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
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

    public List<Customer> customerSearch(UserSearchRequest userSearchRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> customerQuery = builder.createQuery(Customer.class);
        Root<Customer> root = customerQuery.from(Customer.class);
        List<Predicate> predicates = new ArrayList<>();
        expertService.personsInfoInSearching(userSearchRequest, builder, root.get("role"), root.get("firstname"),
                root.get("lastname"), root.get("email"), root.get("dateOfSignUp"), predicates);
        if (userSearchRequest.enabled() != null)
            predicates.add(builder.equal(root.get("enabled"), userSearchRequest.enabled()));

        customerQuery.where(builder.and(predicates.toArray(predicates.toArray(new Predicate[]{}))));

        List<Customer> customerList = entityManager.createQuery(customerQuery).getResultList();
        List<Customer> newList = new ArrayList<>();

        if (userSearchRequest.orderNum() != null) {
            return orderNumInSearching(userSearchRequest, builder, customerList, newList);
        }

        if (userSearchRequest.orderNumInDone() != null) {
            return orderNumInDoneInSearching(userSearchRequest, builder, customerList, newList);
        }

        return customerList;
    }

    private List<Customer> orderNumInSearching(UserSearchRequest userSearchRequest, CriteriaBuilder builder,
                                               List<Customer> customerList, List<Customer> newList) {
        CriteriaQuery<Object[]> orderQuery = builder.createQuery(Object[].class);
        Root<Orders> rootOrder = orderQuery.from(Orders.class);
        orderQuery.multiselect(
                rootOrder.get("customer"),
                builder.count(rootOrder)
        );

        orderQuery.groupBy(rootOrder.get("customer"));
        orderQuery.having(builder.gt(builder.count(rootOrder), userSearchRequest.orderNum()));
        return getCustomers(customerList, newList, orderQuery);
    }

    private List<Customer> orderNumInDoneInSearching(UserSearchRequest userSearchRequest, CriteriaBuilder builder,
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
        orderQuery.having(builder.gt(builder.count(rootOrder), userSearchRequest.orderNumInDone()));
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
