package ir.moslehi.finalprojectphase4.dto.search;

import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.enums.Role;

import java.util.Date;

public record OrderSearchRequest(

    Role role,

    String email,

    OrderStatus orderStatus,

    String serviceName,

    String subServiceName,

    Date orderDateStart,

    Date orderDateFinish

) {
}
