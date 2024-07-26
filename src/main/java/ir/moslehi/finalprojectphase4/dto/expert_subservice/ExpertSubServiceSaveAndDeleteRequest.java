package ir.moslehi.finalprojectphase4.dto.expert_subservice;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertEmailRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceNameRequest;

public record ExpertSubServiceSaveAndDeleteRequest(
        ExpertEmailRequest expert,
        SubServiceNameRequest subService
) {
}
