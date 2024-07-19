package ir.moslehi.finalprojectphase4.dto;

import java.time.LocalDateTime;

public record ExceptionDto(String message,
                           LocalDateTime localDateTime) {
}
