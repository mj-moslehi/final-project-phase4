package ir.moslehi.finalprojectphase4.dto.comment;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerEmailRequest;
import ir.moslehi.finalprojectphase4.dto.expert.ExpertEmailRequest;

public record CommentSaveResponse(
        Long id,
        String comment,
        Double score,
        CustomerEmailRequest customer,
        ExpertEmailRequest expert
) {
}
