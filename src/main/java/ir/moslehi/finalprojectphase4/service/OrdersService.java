package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveRequest;
import ir.moslehi.finalprojectphase4.dto.search.OrderSearchRequest;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.exception.NotValidInput;
import ir.moslehi.finalprojectphase4.mapper.OrderMapper;
import ir.moslehi.finalprojectphase4.model.Customer;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.model.enums.Role;
import ir.moslehi.finalprojectphase4.repository.OrdersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final CustomerService customerService;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;

    @PersistenceContext
    private final EntityManager entityManager;

    public Orders save(OrdersSaveRequest request) {
        Orders mapped = OrderMapper.INSTANCE.ordersSaveRequestToModel(request);
        mapped.setDateOfOrder(validDate(request.dateString()));
        mapped.setSubService(subServiceService.findSubSerBaseOnSer
                (request.ordersServiceAndSubService().service().id()
                        , request.ordersServiceAndSubService().subService().id()));
        mapped.setOrderStatus(OrderStatus.WAITING_FOR_The_SUGGESTION_OF_EXPERTS);
        customerService.findById(mapped.getCustomer().getId());
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

    public Date validDate(String dateString) {
        Date now = new Date();
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (sdf.parse(dateString).after(now))
                date = sdf.parse(dateString);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return date;
    }


    public Orders updateOrderStatus(Orders orders, OrderStatus orderStatus) {
        orders.setOrderStatus(orderStatus);
        ordersRepository.save(orders);
        return orders;
    }

    public List<Orders> ordersSearch(OrderSearchRequest searchRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> orderQuery = builder.createQuery(Orders.class);
        Root<Orders> root = orderQuery.from(Orders.class);
        List<Predicate> predicates = new ArrayList<>();

        if (searchRequest.role() != null) {
            if (searchRequest.role().equals(Role.ROLE_CUSTOMER)) {
                root.join("customer", JoinType.INNER);
                predicates.add(builder.equal(root.get("customer").get("email"), searchRequest.email()));
            } else if (searchRequest.role().equals(Role.ROLE_EXPERT)) {
                root.join("expert", JoinType.INNER);
                predicates.add(builder.equal(root.get("expert").get("email"), searchRequest.email()));
            }
        }

        if (searchRequest.orderStatus() != null)
            predicates.add(builder.equal(root.get("orderStatus"), searchRequest.orderStatus()));

        if (searchRequest.serviceName() != null) {
            ir.moslehi.finalprojectphase4.model.Service service = serviceService.findByName(searchRequest.serviceName());
            root.join("subService", JoinType.INNER);
            predicates.add(builder.equal(root.get("subService").get("service"), service));

        }

        if (searchRequest.subServiceName() != null)
            predicates.add(builder.equal(root.get("subService"), subServiceService.findByName(searchRequest.subServiceName())));

        if (searchRequest.orderDateStart() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("dateOfOrder"), searchRequest.orderDateStart()));
        if (searchRequest.orderDateFinish() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("dateOfOrder"), searchRequest.orderDateFinish()));

        orderQuery.where(builder.and(predicates.toArray(predicates.toArray(new Predicate[]{}))));

        return entityManager.createQuery(orderQuery).getResultList();
    }

}
