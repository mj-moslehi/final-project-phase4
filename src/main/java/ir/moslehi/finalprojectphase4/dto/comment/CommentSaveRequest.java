package ir.moslehi.finalprojectphase4.dto.comment;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertEmailRequest;
import ir.moslehi.finalprojectphase4.dto.orders.OrderIdRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record CommentSaveRequest(

        @Pattern(regexp = "[a-zA-Z0-9., ]{5,100}$")
        String comment,

        @Min(value = 0)
        @Max(value = 5)
        Double score,

        ExpertEmailRequest expert,

        OrderIdRequest orders
) {
}
