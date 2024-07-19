package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.expert_subservice.ExpertSubServiceSaveAndDeleteRequest;
import ir.moslehi.finalprojectphase4.dto.expert_subservice.ExpertSubServiceSaveResponse;
import ir.moslehi.finalprojectphase4.model.ExpertSubService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpertSubServiceMapper {

    ExpertSubServiceMapper INSTANCE = Mappers.getMapper(ExpertSubServiceMapper.class);

    ExpertSubService expertSubServiceSaveAdnDeleteRequestToModel(ExpertSubServiceSaveAndDeleteRequest request);

    ExpertSubServiceSaveResponse modelToExpertSubServiceSaveResponse(ExpertSubService expertSubService);

}
