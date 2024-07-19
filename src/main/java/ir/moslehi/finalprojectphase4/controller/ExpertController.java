package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.expert.*;
import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveResponse;
import ir.moslehi.finalprojectphase4.mapper.ExpertMapper;
import ir.moslehi.finalprojectphase4.mapper.OrderMapper;
import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.service.ExpertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ExpertController {

    private final ExpertService expertService;

    //todo
    @PostMapping("/register-expert")
    public ResponseEntity<ExpertSaveResponse> registerExpert(@Valid @RequestBody ExpertSaveRequest request) {
        Expert mappedExpert = ExpertMapper.INSTANCE.expertSaveRequestToModel(request);
        return new ResponseEntity<>(ExpertMapper.INSTANCE.modelToExpertSaveResponse
                (expertService.register(mappedExpert)), HttpStatus.CREATED);
    }

    @PatchMapping("/update-expert")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<ExpertSaveResponse> updateExpert
            (@Valid @RequestBody ExpertUpdateRequest updateRequest, Principal principal) {
        Expert mappedExpert = ExpertMapper.INSTANCE.expertUpdateRequestToModel(updateRequest);
        Expert expert = expertService.update(mappedExpert, principal.getName());
        return new ResponseEntity<>(ExpertMapper.INSTANCE.modelToExpertSaveResponse(expert), HttpStatus.CREATED);
    }

    //todo
    @PostMapping("/write-image")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void writeImage(@Valid @RequestBody ExpertWriteImage writeImage) {
        expertService.writeImage(writeImage.path(), writeImage.expert().id());
    }

    //todo
    @PatchMapping("/confirming-expert")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ExpertSaveResponse> confirmingExpert(@RequestBody ExpertIdRequest expertId) {
        return new ResponseEntity<>(ExpertMapper.INSTANCE.modelToExpertSaveResponse
                (expertService.confirmedExpertStatus(expertId.id())), HttpStatus.CREATED);
    }

    @GetMapping("/order-history-expert")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<List<OrdersSaveResponse>> orderHistoryForExpert(Principal principal,
                                                                          @RequestParam String orderStatus) {
        return new ResponseEntity<>
                (OrderMapper.INSTANCE.modelListToOrdersSaveResponseList
                        (expertService.ordersHistoryExpert(principal.getName(), orderStatus)), HttpStatus.ACCEPTED);
    }

    //todo
    @GetMapping("/api/expert/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return expertService.confirmToken(token);
    }

    //todo
    @PatchMapping("/read-image")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<ExpertSaveResponse> readImage(Principal principal, @RequestParam MultipartFile file) throws IOException {
        Expert expert = expertService.findByEmail(principal.getName());
        expertService.readImage(expert, file);
        return new ResponseEntity<>(ExpertMapper.INSTANCE.modelToExpertSaveResponse(expert), HttpStatus.CREATED);
    }

    @GetMapping("/see-wallet")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public Long seeWallet(Principal principal){
        return expertService.findByEmail(principal.getName()).getValidity();
    }

}
