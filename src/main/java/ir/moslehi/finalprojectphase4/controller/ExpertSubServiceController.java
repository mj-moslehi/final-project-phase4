package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.expert_subservice.ExpertSubServiceSaveAndDeleteRequest;
import ir.moslehi.finalprojectphase4.dto.expert_subservice.ExpertSubServiceSaveResponse;
import ir.moslehi.finalprojectphase4.mapper.ExpertSubServiceMapper;
import ir.moslehi.finalprojectphase4.model.ExpertSubService;
import ir.moslehi.finalprojectphase4.service.ExpertSubServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpertSubServiceController {

    private final ExpertSubServiceService expertSubServiceService;

    //todo
    @PostMapping("/register-expert-sub-service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ExpertSubServiceSaveResponse> registerExpertSubService
            (@RequestBody ExpertSubServiceSaveAndDeleteRequest request) {
        ExpertSubService mapped = ExpertSubServiceMapper.INSTANCE.expertSubServiceSaveAdnDeleteRequestToModel(request);
        ExpertSubService expertSubService = expertSubServiceService.save(mapped);
        return new ResponseEntity<>(ExpertSubServiceMapper.INSTANCE.
                modelToExpertSubServiceSaveResponse(expertSubService), HttpStatus.CREATED);
    }

    //todo
    @DeleteMapping("/delete-expert-sub-service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteExpertSubService(@RequestBody ExpertSubServiceSaveAndDeleteRequest deleteRequest){
        ExpertSubService mapped = ExpertSubServiceMapper.INSTANCE.expertSubServiceSaveAdnDeleteRequestToModel(deleteRequest);
        expertSubServiceService.removeByExpertAndSubService(mapped.getExpert().getId(),mapped.getSubService().getId());
    }

}
