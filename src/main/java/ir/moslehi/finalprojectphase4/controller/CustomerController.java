package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerSaveRequest;
import ir.moslehi.finalprojectphase4.dto.customer.CustomerSaveResponse;
import ir.moslehi.finalprojectphase4.dto.customer.CustomerUpdateRequest;
import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveResponse;
import ir.moslehi.finalprojectphase4.mapper.CustomerMapper;
import ir.moslehi.finalprojectphase4.mapper.OrderMapper;
import ir.moslehi.finalprojectphase4.model.Customer;
import ir.moslehi.finalprojectphase4.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register-customer")
    public ResponseEntity<CustomerSaveResponse> registerCustomer(@Valid @RequestBody CustomerSaveRequest request) {
        Customer mappedCustomer = CustomerMapper.INSTANCE.customerSaveRequestToModel(request);
        return new ResponseEntity<>(CustomerMapper.INSTANCE.modelToCustomerSaveResponse
                (customerService.register(mappedCustomer)), HttpStatus.CREATED);
    }

    @PatchMapping("/update-customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CustomerSaveResponse> updateCustomer
            (@Valid @RequestBody CustomerUpdateRequest updateRequest,Principal principal) {
        Customer mappedCustomer = CustomerMapper.INSTANCE.customerUpdateRequestToModel(updateRequest);
        Customer customer = customerService.update(mappedCustomer,principal.getName());
        return new ResponseEntity<>(CustomerMapper.INSTANCE.modelToCustomerSaveResponse(customer), HttpStatus.CREATED);
    }

    @GetMapping( "/api/customer/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return customerService.confirmToken(token);
    }

    @GetMapping("/order-history-customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<OrdersSaveResponse>> orderHistoryForCustomer(Principal principal,
                                                                            @RequestParam String orderStatus) {
        return new ResponseEntity<>
                (OrderMapper.INSTANCE.modelListToOrdersSaveResponseList
                        (customerService.ordersHistoryCustomer(principal.getName(), orderStatus)), HttpStatus.ACCEPTED);
    }

    @GetMapping("/customer-see-wallet")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public Long seeWallet(Principal principal){
        return customerService.findByEmail(principal.getName()).getValidity();
    }

}
