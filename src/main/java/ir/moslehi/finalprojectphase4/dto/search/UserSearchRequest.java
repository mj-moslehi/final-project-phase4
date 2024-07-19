package ir.moslehi.finalprojectphase4.dto.search;

import ir.moslehi.finalprojectphase4.model.enums.ExpertStatus;
import ir.moslehi.finalprojectphase4.model.enums.Role;

import java.util.Date;

public record UserSearchRequest(

        Role role,

        String firstname,

        String lastname,

        String email,

        Double score,

        Long validity,

        ExpertStatus expertStatus,

        Date dateOfSignUpStart,

        Date dateOfSignUpEnd,

        Integer orderNum,

        Integer orderNumInDone,

        Boolean enabled,

        String subServiceName

) {
}
