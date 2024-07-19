package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.orders.OrderChoosingExpert;
import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveResponse;
import ir.moslehi.finalprojectphase4.dto.suggestion.SuggestionSaveRequest;
import ir.moslehi.finalprojectphase4.dto.suggestion.SuggestionSaveResponse;
import ir.moslehi.finalprojectphase4.mapper.OrderMapper;
import ir.moslehi.finalprojectphase4.mapper.SuggestionMapper;
import ir.moslehi.finalprojectphase4.model.Customer;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.model.Suggestion;
import ir.moslehi.finalprojectphase4.service.CustomerService;
import ir.moslehi.finalprojectphase4.service.OrdersService;
import ir.moslehi.finalprojectphase4.service.SuggestionService;
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
public class SuggestionController {

    private final SuggestionService suggestionService;
    private final OrdersService ordersService;
    private final CustomerService customerService;

    @PostMapping("/register-suggestion")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<SuggestionSaveResponse> registerSuggestion
            (@Valid @RequestBody SuggestionSaveRequest request) {
        Suggestion suggestion = suggestionService.save(request);
        ordersService.updateOrderStatus(suggestion.getOrders(), OrderStatus.WAITING_FOR_SPECIALIST_SELECTION);
        return new ResponseEntity<>
                (SuggestionMapper.INSTANCE.modelToSuggestionSaveResponse(suggestion), HttpStatus.CREATED);
    }

    @PatchMapping("/choosing-expert")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrdersSaveResponse> choosingExpert(@RequestBody OrderChoosingExpert choosingExpert,Principal principal) {
        Orders orders = suggestionService.validExpertForOrder
                (choosingExpert.expert().id(), choosingExpert.order().id(), principal.getName());
        return new ResponseEntity<>(OrderMapper.INSTANCE.modelToOrdersSaveResponse(orders), HttpStatus.CREATED);
    }

    @PatchMapping("/update-order-status-to-started")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrdersSaveResponse> updateOrderStatusToStarted(@RequestParam Long orderId, Principal principal) {
        Orders orders = suggestionService.updateOrderStatusToStarted(principal.getName(),orderId);
        return new ResponseEntity<>(OrderMapper.INSTANCE.modelToOrdersSaveResponse(orders), HttpStatus.CREATED);
    }

    @PatchMapping("/update-order-status-to-done")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrdersSaveResponse> updateOrderStatusToDone(@RequestParam Long orderId, Principal principal) {
        Orders orders = suggestionService.updateOrderStatusToDone(principal.getName(), orderId);
        return new ResponseEntity<>(OrderMapper.INSTANCE.modelToOrdersSaveResponse(orders), HttpStatus.CREATED);
    }

    @PatchMapping("/paying-from-validity")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrdersSaveResponse> payingFromValidity(@RequestParam Long orderId, Principal principal) {
        Orders orders = suggestionService.validity(principal.getName(), orderId);
        return new ResponseEntity<>(OrderMapper.INSTANCE.modelToOrdersSaveResponse(orders), HttpStatus.CREATED);
    }

    @GetMapping("/see-valid-order")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<List<OrdersSaveResponse>> seeValidOrder(Principal principal) {
        return new ResponseEntity<>(OrderMapper.INSTANCE.modelListToOrdersSaveResponseList
                (suggestionService.validOrdersForExpert(principal.getName())), HttpStatus.FOUND);
    }

    @GetMapping("/sorted-suggestions")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<SuggestionSaveResponse>> sortedSuggestionForCustomer
            (Principal principal, @RequestParam Long orderId) {
        Customer customer = customerService.findByEmail(principal.getName());
        Orders orders = suggestionService.validOrderForCustomerWithOrderStatus
                (OrderStatus.WAITING_FOR_SPECIALIST_SELECTION, customer, orderId);

        return new ResponseEntity<>
                (SuggestionMapper.INSTANCE.modelListToSuggestionSaveResponseList
                        (suggestionService.suggestionListSorted(orders.getId())), HttpStatus.FOUND);
    }

}
