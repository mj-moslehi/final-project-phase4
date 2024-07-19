package ir.moslehi.finalprojectphase4.dto.subService;

import ir.moslehi.finalprojectphase4.dto.service.ServiceIdRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record SubServiceSaveRequest(

        @Pattern(regexp = "^[a-zA-Z ]{3,15}$")
        String name,

        @Pattern(regexp = "[a-zA-Z0-9., ]{5,100}$")
        String description,

        @Min(value = 0 )
        Long basePrice,

        ServiceIdRequest service

) {
}
