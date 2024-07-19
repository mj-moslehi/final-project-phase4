package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.service.ServiceSaveRequest;
import ir.moslehi.finalprojectphase4.dto.service.ServiceSaveResponse;
import ir.moslehi.finalprojectphase4.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    Service serviceSaveRequestToModel(ServiceSaveRequest request);

    ServiceSaveResponse modelToServiceSaveResponse(Service service);

}
