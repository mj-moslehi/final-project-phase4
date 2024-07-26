package ir.moslehi.finalprojectphase4.dto.orders;

import ir.moslehi.finalprojectphase4.dto.service.ServiceNameRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceNameRequest;

public record OrdersServiceAndSubServiceRequest(
        ServiceNameRequest service,
        SubServiceNameRequest subService
){
}
