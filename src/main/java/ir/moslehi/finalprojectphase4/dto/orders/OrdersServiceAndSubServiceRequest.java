package ir.moslehi.finalprojectphase4.dto.orders;

import ir.moslehi.finalprojectphase4.dto.service.ServiceIdRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceIdRequest;

public record OrdersServiceAndSubServiceRequest(
        ServiceIdRequest service,
        SubServiceIdRequest subService
){
}
