package ir.moslehi.finalprojectphase4.dto.suggestion;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertEmailRequest;
import ir.moslehi.finalprojectphase4.dto.orders.OrderIdRequest;

import java.util.Date;

public record SuggestionSaveResponse(
        Long id,
        Date finishDate,
        Date startDate,
        Long proposedPrice,
        ExpertEmailRequest expert,
        OrderIdRequest orders
) {
}
