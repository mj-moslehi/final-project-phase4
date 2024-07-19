package ir.moslehi.finalprojectphase4.controller;


import ir.moslehi.finalprojectphase4.dto.subService.SubServiceSaveRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceSaveResponse;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceUpdateRequest;
import ir.moslehi.finalprojectphase4.dto.subService.SubServiceUpdateResponse;
import ir.moslehi.finalprojectphase4.mapper.SubServiceMapper;
import ir.moslehi.finalprojectphase4.model.SubService;
import ir.moslehi.finalprojectphase4.service.SubServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubServiceController {

    private final SubServiceService subServiceService;

    @PostMapping("/register-sub-service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SubServiceSaveResponse> registerSubService
            (@Valid @RequestBody SubServiceSaveRequest request) {
        SubService mappedSubService = SubServiceMapper.INSTANCE.subServiceSaveRequestToModel(request);
        SubService subService = subServiceService.save(mappedSubService);
        return new ResponseEntity<>(SubServiceMapper.INSTANCE.modelToSubServiceSaveResponse(subService)
                , HttpStatus.CREATED);
    }

    @PatchMapping("/update-sub-service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SubServiceUpdateResponse> updateSubService
            (@Valid @RequestBody SubServiceUpdateRequest updateRequest) {
        SubService mappedSubService = SubServiceMapper.INSTANCE.subServiceUpdateRequestToModel(updateRequest);
        SubService subService = subServiceService.update(mappedSubService);
        return new ResponseEntity<>(SubServiceMapper.INSTANCE.modelToSubServiceUpdateResponse(subService)
                , HttpStatus.CREATED);
    }

}
