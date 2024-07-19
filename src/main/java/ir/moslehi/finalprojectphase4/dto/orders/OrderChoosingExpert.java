package ir.moslehi.finalprojectphase4.dto.orders;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertIdRequest;

public record OrderChoosingExpert(
        OrderIdRequest order,
        ExpertIdRequest expert
) {

}
