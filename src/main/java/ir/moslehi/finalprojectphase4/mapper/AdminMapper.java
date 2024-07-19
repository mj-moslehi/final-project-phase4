package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.admin.AdminSaveRequest;
import ir.moslehi.finalprojectphase4.dto.admin.AdminSaveResponse;
import ir.moslehi.finalprojectphase4.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);
    Admin adminSaveRequestToModel(AdminSaveRequest request);
    AdminSaveResponse modelToAdminSaveResponse(Admin admin);

}
