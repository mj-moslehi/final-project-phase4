package ir.moslehi.finalprojectphase4.dto.expert;

import ir.moslehi.finalprojectphase4.model.enums.ExpertStatus;
import ir.moslehi.finalprojectphase4.model.enums.Role;

import java.util.Date;
import java.util.Random;

public record ExpertSaveResponse(
        Long id,
        String firstname,
        String lastname,
        String password,
        String email,
        Date dateOfSignUp,
        Double score,
        Long validity,
        ExpertStatus expertStatus,
        boolean enabled,
        Role role
) {
}
