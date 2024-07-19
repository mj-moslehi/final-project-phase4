package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.subService.SubServiceSaveRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceSaveResponse;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceUpdateRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceUpdateResponse;
import ir.moslehi.finalprojectphase4.model.SubService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubServiceMapper {

    SubServiceMapper INSTANCE = Mappers.getMapper(SubServiceMapper.class);

    SubService subServiceSaveRequestToModel(SubServiceSaveRequest request);

    SubServiceSaveResponse modelToSubServiceSaveResponse(SubService subService);

    SubService subServiceUpdateRequestToModel(SubServiceUpdateRequest updateRequest);

    SubServiceUpdateResponse modelToSubServiceUpdateResponse(SubService subService);

}
