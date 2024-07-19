package ir.moslehi.finalprojectphase4.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record CustomerUpdateRequest(

        @Email
        String email,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$")
        String password

) {
}
