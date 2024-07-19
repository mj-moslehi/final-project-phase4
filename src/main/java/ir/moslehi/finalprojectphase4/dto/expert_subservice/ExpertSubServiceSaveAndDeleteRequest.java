package ir.moslehi.finalprojectphase4.dto.expert_subservice;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertIdRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceIdRequest;

public record ExpertSubServiceSaveAndDeleteRequest(
        ExpertIdRequest expert,
        SubServiceIdRequest subService
) {
}
