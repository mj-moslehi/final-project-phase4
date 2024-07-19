package ir.moslehi.finalprojectphase4.dto.comment;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerIdRequest;
import ir.moslehi.finalprojectphase4.dto.expert.ExpertIdRequest;

public record CommentSaveResponse(
        Long id,
        String comment,
        Double score,
        CustomerIdRequest customer,
        ExpertIdRequest expert
) {
}
