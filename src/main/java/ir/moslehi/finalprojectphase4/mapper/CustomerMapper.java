package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerSaveRequest;
import ir.moslehi.finalprojectphase4.dto.customer.CustomerSaveResponse;
import ir.moslehi.finalprojectphase4.dto.customer.CustomerUpdateRequest;
import ir.moslehi.finalprojectphase4.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer customerSaveRequestToModel(CustomerSaveRequest request);

    CustomerSaveResponse modelToCustomerSaveResponse(Customer customer);

    Customer customerUpdateRequestToModel(CustomerUpdateRequest updateRequest);

}
