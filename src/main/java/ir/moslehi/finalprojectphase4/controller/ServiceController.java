package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.service.ServiceSaveRequest;
import ir.moslehi.finalprojectphase4.dto.service.ServiceSaveResponse;
import ir.moslehi.finalprojectphase4.mapper.ServiceMapper;
import ir.moslehi.finalprojectphase4.model.Service;
import ir.moslehi.finalprojectphase4.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class ServiceController {

    private final ServiceService serviceService;

    //todo
    @PostMapping("/register-service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceSaveResponse> registerService(@Valid @RequestBody ServiceSaveRequest request){
        Service mappedService = ServiceMapper.INSTANCE.serviceSaveRequestToModel(request);
        Service service = serviceService.save(mappedService);
        return new ResponseEntity<>(ServiceMapper.INSTANCE.modelToServiceSaveResponse(service), HttpStatus.CREATED);
    }

}
