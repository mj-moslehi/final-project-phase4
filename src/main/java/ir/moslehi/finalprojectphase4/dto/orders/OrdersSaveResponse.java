package ir.moslehi.finalprojectphase4.dto.orders;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerEmailRequest;
import ir.moslehi.finalprojectphase4.dto.expert.ExpertEmailRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceNameRequest;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;

import java.util.Date;

public record OrdersSaveResponse(
        Long id,
        String address,
        Date dateOfOrder,
        String description,
        OrderStatus orderStatus,
        Long proposedPrice,
        CustomerEmailRequest customer,
        SubServiceNameRequest subService,
        ExpertEmailRequest expert
) {
}
