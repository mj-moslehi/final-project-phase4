package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveRequest;
import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveResponse;
import ir.moslehi.finalprojectphase4.mapper.OrderMapper;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping("/register-order")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrdersSaveResponse> registerOrders(@Valid @RequestBody OrdersSaveRequest request) {
        Orders orders = ordersService.save(request);
        return new ResponseEntity<>(OrderMapper.INSTANCE.modelToOrdersSaveResponse(orders), HttpStatus.CREATED);
    }

}
