package ir.moslehi.finalprojectphase4.dto.cartInfo;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerEmailRequest;

import java.util.Date;

public record CartInfoSaveResponse(
        String cartNumber,
        String cvv2,
        String password,
        Date expirationDate,
        CustomerEmailRequest customer
) {
}
