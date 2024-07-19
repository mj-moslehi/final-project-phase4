package ir.moslehi.finalprojectphase4.dto.expert_subservice;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertIdRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceIdRequest;

public record ExpertSubServiceSaveResponse(
        Long id,
        ExpertIdRequest expert,
        SubServiceIdRequest subService
) {
}
