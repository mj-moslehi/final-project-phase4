package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveRequest;
import ir.moslehi.finalprojectphase4.dto.search.AdminSearchingRequest;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.exception.NotValidInput;
import ir.moslehi.finalprojectphase4.mapper.OrderMapper;
import ir.moslehi.finalprojectphase4.model.Customer;
import ir.moslehi.finalprojectphase4.model.SubService;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.repository.OrdersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final CustomerService customerService;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;

    @PersistenceContext
    private final EntityManager entityManager;

    public Orders save(OrdersSaveRequest request) throws ParseException {
        Orders mapped = OrderMapper.INSTANCE.ordersSaveRequestToModel(request);
        mapped.setDateOfOrder(validDate(request.dateString()));
        mapped.setSubService(subServiceService.findSubSerBaseOnSer
                (request.ordersServiceAndSubService().service().name()
                        , request.ordersServiceAndSubService().subService().name()));
        mapped.setOrderStatus(OrderStatus.WAITING_FOR_The_SUGGESTION_OF_EXPERTS);
        mapped.setCustomer(customerService.findByEmail(request.customer().email()));
        if (mapped.getDateOfOrder() == null)
            throw new NotValidInput("the date isn't valid");
        return ordersRepository.save(mapped);
    }

    public Orders findById(Long id) {
        return ordersRepository.findById(id).orElseThrow(
                () -> new NotFoundException("the order wasn't found")
        );
    }

    public List<Orders> findByCustomerAndOrderStatus(Customer customer, OrderStatus orderStatus) {
        return ordersRepository.findByCustomerAndOrderStatus(customer, orderStatus);
    }

    public Date validDate(String dateString) throws ParseException {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (sdf.parse(dateString).after(now))
            return sdf.parse(dateString);
        else throw new NotValidInput("the date is before now");
    }

    public List<Orders> findBySubServiceAndValidOrderStatus(SubService subService) {
        if (!ordersRepository.findBySubServiceAndValidOrderStatus(subService).isEmpty())
            return ordersRepository.findBySubServiceAndValidOrderStatus(subService);
        else throw new NotFoundException("there isn't any order with these order status");
    }

    public Orders updateOrderStatus(Orders orders, OrderStatus orderStatus) {
        orders.setOrderStatus(orderStatus);
        ordersRepository.save(orders);
        return orders;
    }

    public List<Orders> ordersSearch(AdminSearchingRequest searchRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> orderQuery = builder.createQuery(Orders.class);
        Root<Orders> root = orderQuery.from(Orders.class);
        List<Predicate> predicates = new ArrayList<>();

        if (searchRequest.role() != null) {
            root.join("customer", JoinType.INNER);
            predicates.add(builder.equal(root.get("customer").get("role"), searchRequest.role()));
            root.join("expert", JoinType.INNER);
            predicates.add(builder.equal(root.get("expert").get("role"), searchRequest.role()));
        }

        if (searchRequest.email() != null) {
            root.join("customer", JoinType.INNER);
            predicates.add(builder.equal(root.get("customer").get("email"), searchRequest.email()));
            root.join("expert", JoinType.INNER);
            predicates.add(builder.equal(root.get("expert").get("email"), searchRequest.email()));
        }

        if (searchRequest.orderStatus() != null)
            predicates.add(builder.equal(root.get("orderStatus"), searchRequest.orderStatus()));

        if (searchRequest.orderServiceName() != null) {
            ir.moslehi.finalprojectphase4.model.Service service =
                    serviceService.findByName(searchRequest.orderServiceName());
            root.join("subService", JoinType.INNER);
            predicates.add(builder.equal(root.get("subService").get("service"), service));
        }

        if (searchRequest.orderSubServiceName() != null)
            predicates.add(builder.equal(root.get("subService"),
                    subServiceService.findByName(searchRequest.orderSubServiceName())));

        if (searchRequest.orderDateStart() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("dateOfOrder"), searchRequest.orderDateStart()));

        if (searchRequest.orderDateFinish() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("dateOfOrder"), searchRequest.orderDateFinish()));

        orderQuery.where(builder.and(predicates.toArray(predicates.toArray(new Predicate[]{}))));

        return entityManager.createQuery(orderQuery).getResultList();
    }

}
