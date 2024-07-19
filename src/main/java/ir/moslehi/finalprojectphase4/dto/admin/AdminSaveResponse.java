package ir.moslehi.finalprojectphase4.dto.admin;

public record AdminSaveResponse(
        Long id,
        String firstname,
        String lastname,
        String email,
        String password) {
}
