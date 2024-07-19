package ir.moslehi.finalprojectphase4.dto.orders;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerIdRequest;
import ir.moslehi.finalprojectphase4.dto.expert.ExpertIdRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceIdRequest;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;

import java.util.Date;

public record OrdersSaveResponse(
        Long id,
        String address,
        Date dateOfOrder,
        String description,
        OrderStatus orderStatus,
        Long proposedPrice,
        CustomerIdRequest customer,
        SubServiceIdRequest subService,
        ExpertIdRequest expert
) {
}
