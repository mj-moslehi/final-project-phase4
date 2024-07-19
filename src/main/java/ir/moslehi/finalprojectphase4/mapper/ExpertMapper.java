package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.expert.ExpertSaveRequest;
import ir.moslehi.finalprojectphase4.dto.expert.ExpertSaveResponse;
import ir.moslehi.finalprojectphase4.dto.expert.ExpertUpdateRequest;
import ir.moslehi.finalprojectphase4.model.Expert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpertMapper {

    ExpertMapper INSTANCE = Mappers.getMapper(ExpertMapper.class);

    Expert expertSaveRequestToModel(ExpertSaveRequest request);

    ExpertSaveResponse modelToExpertSaveResponse(Expert expert);

    Expert expertUpdateRequestToModel(ExpertUpdateRequest updateRequest);
}
