package ir.moslehi.finalprojectphase4.dto.subService;

import ir.moslehi.finalprojectphase4.model.Service;

public record SubServiceSaveResponse(
        Long id,
        String name,
        String description,
        Long basePrice,
        Service service) {
}
