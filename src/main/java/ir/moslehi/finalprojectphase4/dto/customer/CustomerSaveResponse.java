package ir.moslehi.finalprojectphase4.dto.customer;

import ir.moslehi.finalprojectphase4.model.enums.Role;

import java.util.Date;

public record CustomerSaveResponse(
        Long id,
        String firstname,
        String lastname,
        String email,
        String password,
        Date dateOfSignUp,
        Long validity,
        boolean enabled,
        Role role
) {
}
