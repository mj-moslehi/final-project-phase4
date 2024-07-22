package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSaveRequest;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSaveResponse;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSignInRequest;
import ir.moslehi.finalprojectphase4.mapper.CartInfoMapper;
import ir.moslehi.finalprojectphase4.model.CartInfo;
import ir.moslehi.finalprojectphase4.service.CartInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Validated
public class CartInfoController {

    private final CartInfoService cartInfoService;
    private Long cartInfoId;

    @PostMapping("/register-cart-info")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartInfoSaveResponse> registerCartInfo(@Valid @RequestBody CartInfoSaveRequest request) {
        CartInfo bankCart = cartInfoService.save(request);
        return new ResponseEntity<>(CartInfoMapper.INSTANCE.modelToCartInfoSaveResponse(bankCart), HttpStatus.CREATED);
    }

    @PostMapping("/payment-cart-info")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartInfoSaveResponse> paymentCartInfo
            (@Valid @RequestBody CartInfoSignInRequest signInRequest) throws IOException {
        CartInfo cartInfo = cartInfoService.payment(signInRequest);
        cartInfoId = cartInfo.getId();
        return new ResponseEntity<>(CartInfoMapper.INSTANCE.modelToCartInfoSaveResponse(cartInfo), HttpStatus.OK);
    }

    @GetMapping("/captcha-image")
    public ResponseEntity<byte[]> captchaImage() {
        byte[] imageData = cartInfoService.findById(cartInfoId).getCaptchaImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

}
