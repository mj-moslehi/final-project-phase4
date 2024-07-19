package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSaveRequest;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSignInRequest;
import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.exception.NotValidInput;
import ir.moslehi.finalprojectphase4.mapper.CartInfoMapper;
import ir.moslehi.finalprojectphase4.model.CartInfo;
import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.repository.CartInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartInfoService {

    private final CartInfoRepository cartInfoRepository;
    private final SuggestionService suggestionService;
    private final OrdersService ordersService;
    private final CustomerService customerService;
    private final ExpertService expertService;

    public CartInfo save(CartInfoSaveRequest request) {
        CartInfo mapped = CartInfoMapper.INSTANCE.cartInfoSaveRequestToModel(request);
        mapped.setExpirationDate(validDate(request.stringDate()));
        mapped.setCustomer(customerService.findById(request.customer().id()));
        if (cartInfoRepository.findByCartNumberAndCvv2(mapped.getCartNumber(), mapped.getCvv2()).isPresent())
            throw new DuplicateInformationException("the cart is duplicate");
        if (mapped.getExpirationDate() == null)
            throw new NotValidInput("the expiration date wasn't valid");
        return cartInfoRepository.save(mapped);
    }

    public Date validDate(String dateString) {
        Date now = new Date();
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            if (sdf.parse(dateString).after(now))
                date = sdf.parse(dateString);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return date;
    }

    public CartInfo payment(CartInfoSignInRequest signInRequest, Long orderId) {
        CartInfo mappedCartInfo = CartInfoMapper.INSTANCE.cartInfoSignInRequestToModel(signInRequest);
        mappedCartInfo.setExpirationDate(validDate(signInRequest.stringDate()));
        if (cartInfoRepository.findByCartNumberAndCvv2
                (mappedCartInfo.getCartNumber(), mappedCartInfo.getCvv2()).isPresent()) {
            CartInfo foundCartInfo =
                    cartInfoRepository.findByCartNumberAndCvv2
                            (mappedCartInfo.getCartNumber(), mappedCartInfo.getCvv2()).get();

            return checkExpirationDate(mappedCartInfo, foundCartInfo, orderId);

        } else throw new NotValidInput("cart number or cvv2 wasn't found");
    }

    public CartInfo checkExpirationDate(CartInfo cartInfo, CartInfo foundCartInfo, Long orderId) {
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
            ordersService.updateOrderStatus(orders, OrderStatus.PAID);
            return foundCartInfo;
        } else throw new NotValidInput("the password or expiration date wasn't valid");
    }

    public void addMoneyToExpert(Orders orders) {
        Expert expert = orders.getExpert();
        expertService.updateValidity(expert,
                (suggestionService.findByOrdersAndExpert(orders, expert).getProposedPrice() * 7) / 10);
    }

}