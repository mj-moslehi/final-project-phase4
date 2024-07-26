package ir.moslehi.finalprojectphase4.dto.suggestion;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertEmailRequest;
import ir.moslehi.finalprojectphase4.dto.orders.OrderIdRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record SuggestionSaveRequest(

        @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]$")
        String dateStringFinish,

        @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]$")
        String dateStringStart,

        @Min(value = 0)
        Long proposedPrice,

        ExpertEmailRequest expert,

        OrderIdRequest orders
        ) {
}
