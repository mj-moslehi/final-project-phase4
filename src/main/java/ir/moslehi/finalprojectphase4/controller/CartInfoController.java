package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSaveRequest;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSaveResponse;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSignInRequest;
import ir.moslehi.finalprojectphase4.mapper.CartInfoMapper;
import ir.moslehi.finalprojectphase4.model.CartInfo;
import ir.moslehi.finalprojectphase4.service.CartInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class CartInfoController {

    private final CartInfoService cartInfoService;

    //todo
    @PostMapping("/register-cart-info")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartInfoSaveResponse> registerCartInfo(@Valid @RequestBody CartInfoSaveRequest request) {
        CartInfo bankCart = cartInfoService.save(request);
        return new ResponseEntity<>(CartInfoMapper.INSTANCE.modelToCartInfoSaveResponse(bankCart), HttpStatus.CREATED);
    }

    //todo
    @PostMapping("/payment-cart-info")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartInfoSaveResponse> paymentCartInfo(@Valid @RequestBody CartInfoSignInRequest signInRequest) {
        CartInfo bankCart = cartInfoService.payment(signInRequest, signInRequest.order().id());
        return new ResponseEntity<>(CartInfoMapper.INSTANCE.modelToCartInfoSaveResponse(bankCart), HttpStatus.OK);
    }

}
