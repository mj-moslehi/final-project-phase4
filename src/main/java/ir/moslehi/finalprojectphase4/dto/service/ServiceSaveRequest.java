package ir.moslehi.finalprojectphase4.dto.service;

import jakarta.validation.constraints.Pattern;

public record ServiceSaveRequest(

        @Pattern(regexp = "^[a-zA-Z ]{3,15}$")
        String name) {
}
