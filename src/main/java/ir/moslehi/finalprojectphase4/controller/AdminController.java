package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.admin.AdminSaveRequest;
import ir.moslehi.finalprojectphase4.dto.admin.AdminSaveResponse;
import ir.moslehi.finalprojectphase4.dto.customer.CustomerSaveResponse;
import ir.moslehi.finalprojectphase4.dto.expert.ExpertSaveResponse;
import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveResponse;
import ir.moslehi.finalprojectphase4.dto.search.OrderSearchRequest;
import ir.moslehi.finalprojectphase4.dto.search.UserSearchRequest;
import ir.moslehi.finalprojectphase4.dto.search.UserSearchResponse;
import ir.moslehi.finalprojectphase4.mapper.AdminMapper;
import ir.moslehi.finalprojectphase4.mapper.CustomerMapper;
import ir.moslehi.finalprojectphase4.mapper.ExpertMapper;
import ir.moslehi.finalprojectphase4.mapper.OrderMapper;
import ir.moslehi.finalprojectphase4.model.Admin;
import ir.moslehi.finalprojectphase4.service.AdminService;
import ir.moslehi.finalprojectphase4.service.CustomerService;
import ir.moslehi.finalprojectphase4.service.ExpertService;
import ir.moslehi.finalprojectphase4.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final ExpertService expertService;
    private final OrdersService ordersService;

    //todo
    @PostMapping("/register-admin")
    public ResponseEntity<AdminSaveResponse> registerAdmin(@Valid @RequestBody AdminSaveRequest request) {
        Admin mappedAdmin = AdminMapper.INSTANCE.adminSaveRequestToModel(request);
        Admin savedAdmin = adminService.save(mappedAdmin);
        return new ResponseEntity<>(AdminMapper.INSTANCE.modelToAdminSaveResponse(savedAdmin), HttpStatus.CREATED);
    }

    //todo
    @GetMapping("/person-search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserSearchResponse> personSearch(@RequestBody UserSearchRequest request) {
        List<CustomerSaveResponse> customerSaveResponseList = customerService.customerSearch(request)
                .stream()
                .map(CustomerMapper.INSTANCE::modelToCustomerSaveResponse)
                .toList();
        List<ExpertSaveResponse> technicianSaveResponseList = expertService.expertSearch(request)
                .stream()
                .map(ExpertMapper.INSTANCE::modelToExpertSaveResponse)
                .toList();
        List<Object> userList = new ArrayList<>(customerSaveResponseList);
        userList.addAll(technicianSaveResponseList);
        return new ResponseEntity<>(new UserSearchResponse(userList), HttpStatus.FOUND);
    }

    //todo
    @GetMapping("/order-search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OrdersSaveResponse>> orderSearch(@RequestBody OrderSearchRequest request) {
        List<OrdersSaveResponse> ordersSaveResponseList = ordersService.ordersSearch(request)
                .stream()
                .map(OrderMapper.INSTANCE::modelToOrdersSaveResponse)
                .toList();
        return new ResponseEntity<>(ordersSaveResponseList, HttpStatus.FOUND);
    }

}