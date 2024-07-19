package ir.moslehi.finalprojectphase4.dto.expert;

import jakarta.validation.constraints.Pattern;

public record ExpertWriteImage(

        @Pattern(regexp = ".*\\.(jpg|jpeg)$")
        String path,

        ExpertIdRequest expert
) {
}
