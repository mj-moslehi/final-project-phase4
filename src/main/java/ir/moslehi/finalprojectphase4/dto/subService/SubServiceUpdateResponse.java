package ir.moslehi.finalprojectphase4.dto.subService;

public record SubServiceUpdateResponse(
        Long id,
        String name,
        String description,
        Long basePrice
) {
}
