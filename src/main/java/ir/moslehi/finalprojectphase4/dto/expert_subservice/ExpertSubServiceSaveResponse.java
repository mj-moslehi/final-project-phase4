package ir.moslehi.finalprojectphase4.dto.expert_subservice;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertEmailRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceNameRequest;

public record ExpertSubServiceSaveResponse(
        Long id,
        ExpertEmailRequest expert,
        SubServiceNameRequest subService
) {
}
