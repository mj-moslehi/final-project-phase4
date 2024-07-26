package ir.moslehi.finalprojectphase4.dto.search;

import ir.moslehi.finalprojectphase4.model.enums.ExpertStatus;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import ir.moslehi.finalprojectphase4.model.enums.Role;

import java.util.Date;

public record AdminSearchingRequest(

        Role role,

        String firstname,

        String lastname,

        String email,

        Double score,

        Long validity,

        ExpertStatus expertStatus,

        Date dateOfSignUpStart,

        Date dateOfSignUpEnd,

        Integer orderNumForUser,

        Integer orderNumInDoneForUser,

        Boolean userEnabled,

        String expertSubServiceName,

//        Boolean searchingInOrders,

        OrderStatus orderStatus,

        String orderServiceName,

        String orderSubServiceName,

        Date orderDateStart,

        Date orderDateFinish

) {
}
