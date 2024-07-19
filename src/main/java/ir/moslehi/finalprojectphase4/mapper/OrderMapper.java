package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveRequest;
import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveResponse;
import ir.moslehi.finalprojectphase4.model.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Orders ordersSaveRequestToModel(OrdersSaveRequest request);

    OrdersSaveResponse modelToOrdersSaveResponse(Orders orders);

    List<OrdersSaveResponse> modelListToOrdersSaveResponseList(List<Orders> ordersList);

}
