package ir.moslehi.finalprojectphase4.dto.orders;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerIdRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record OrdersSaveRequest(

        @Pattern(regexp = "[a-zA-Z0-9., ]{5,100}$")
        String address,

        @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]$")
        String dateString,

        @Pattern(regexp = "[a-zA-Z0-9., ]{5,100}$")
        String description,

        @Min(value = 0)
        Long proposedPrice,

        OrdersServiceAndSubServiceRequest ordersServiceAndSubService,

        CustomerIdRequest customer

        ) {
}
