package ir.moslehi.finalprojectphase4.dto.orders;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertEmailRequest;

public record OrderChoosingExpert(
        OrderIdRequest order,
        ExpertEmailRequest expert
) {

}
