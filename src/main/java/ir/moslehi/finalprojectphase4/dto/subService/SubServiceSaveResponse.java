package ir.moslehi.finalprojectphase4.dto.subService;


import ir.moslehi.finalprojectphase4.dto.service.ServiceSaveResponse;

public record SubServiceSaveResponse(
        Long id,
        String name,
        String description,
        Long basePrice,
        ServiceSaveResponse service
) {
}
