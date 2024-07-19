package ir.moslehi.finalprojectphase4.dto.expert;

import ir.moslehi.finalprojectphase4.model.enums.ExpertStatus;

import java.util.Date;

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
        boolean enabled
) {
}
