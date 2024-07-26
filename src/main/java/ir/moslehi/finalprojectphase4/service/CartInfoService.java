package ir.moslehi.finalprojectphase4.service;

import com.mewebstudio.captcha.Captcha;
import com.mewebstudio.captcha.GeneratedCaptcha;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSaveRequest;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSignInRequest;
import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.exception.NotValidInput;
import ir.moslehi.finalprojectphase4.mapper.CartInfoMapper;
import ir.moslehi.finalprojectphase4.model.CartInfo;
import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.repository.CartInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CartInfoService {

    private final CartInfoRepository cartInfoRepository;
    private final SuggestionService suggestionService;
    private final OrdersService ordersService;
    private final CustomerService customerService;
    private final ExpertService expertService;

    public CartInfo save(CartInfoSaveRequest request, String customerEmail) throws ParseException {
        CartInfo mapped = CartInfoMapper.INSTANCE.cartInfoSaveRequestToModel(request);
        mapped.setExpirationDate(validDate(request.stringDate()));
        mapped.setCustomer(customerService.findByEmail(customerEmail));
        if (cartInfoRepository.findByCartNumberAndCvv2(mapped.getCartNumber(), mapped.getCvv2()).isPresent())
            throw new DuplicateInformationException("the cart is duplicate");
        if (mapped.getExpirationDate() == null)
            throw new NotValidInput("the expiration date wasn't valid");
        return cartInfoRepository.save(mapped);
    }

    public CartInfo findById(Long id) {
        return cartInfoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("the cart info with id :" + id + "wasn't found")
        );
    }

    public Date validDate(String dateString) throws ParseException {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (sdf.parse(dateString).after(now))
            return sdf.parse(dateString);
        else throw new NotValidInput("the expiration date should be after now");
    }

    public CartInfo payment(CartInfoSignInRequest signInRequest) throws IOException, ParseException {
        CartInfo mappedCartInfo = CartInfoMapper.INSTANCE.cartInfoSignInRequestToModel(signInRequest);
        mappedCartInfo.setExpirationDate(validDate(signInRequest.stringDate()));
        if (cartInfoRepository.findByCartNumberAndCvv2
                (mappedCartInfo.getCartNumber(), mappedCartInfo.getCvv2()).isPresent()) {
            CartInfo foundCartInfo =
                    cartInfoRepository.findByCartNumberAndCvv2
                            (mappedCartInfo.getCartNumber(), mappedCartInfo.getCvv2()).get();

            return checkExpirationDate(mappedCartInfo, foundCartInfo, signInRequest.order().id());

        } else throw new NotValidInput("cart number or cvv2 wasn't found");
    }

    public CartInfo checkExpirationDate(CartInfo cartInfo, CartInfo foundCartInfo, Long orderId) throws IOException {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(foundCartInfo.getExpirationDate());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(cartInfo.getExpirationDate());

        if (foundCartInfo.getPassword().equals(cartInfo.getPassword()) &&
                calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
            Orders orders = suggestionService.validOrderForCustomerWithOrderStatus
                    (OrderStatus.DONE, foundCartInfo.getCustomer(), orderId);
            addMoneyToExpert(orders);
            captchaImage(foundCartInfo);
            ordersService.updateOrderStatus(orders, OrderStatus.PAID);
            return foundCartInfo;
        } else throw new NotValidInput("the password or expiration date wasn't valid");
    }

    public void addMoneyToExpert(Orders orders) {
        Expert expert = orders.getExpert();
        expertService.updateValidity(expert,
                (long) (suggestionService.findByOrdersAndExpert(orders, expert).getProposedPrice() * 0.7));
    }

    public void captchaImage(CartInfo cartInfo) throws IOException {
        com.mewebstudio.captcha.Captcha captcha = new Captcha();
        GeneratedCaptcha generatedCaptcha = captcha.generate();
        BufferedImage captchaImage = generatedCaptcha.getImage();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "jpg", byteArrayOutputStream);
        cartInfo.setCaptchaImage(byteArrayOutputStream.toByteArray());
        cartInfo.setHiddenCaptcha(generatedCaptcha.getCode());
    }

}