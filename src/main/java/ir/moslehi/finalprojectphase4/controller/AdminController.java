package ir.moslehi.finalprojectphase4.controller;

import ir.moslehi.finalprojectphase4.dto.admin.AdminSaveRequest;
import ir.moslehi.finalprojectphase4.dto.admin.AdminSaveResponse;
import ir.moslehi.finalprojectphase4.dto.search.AdminSearchingRequest;
import ir.moslehi.finalprojectphase4.dto.search.AdminSearchingResponse;
import ir.moslehi.finalprojectphase4.mapper.AdminMapper;
import ir.moslehi.finalprojectphase4.model.Admin;
import ir.moslehi.finalprojectphase4.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/register-admin")
    public ResponseEntity<AdminSaveResponse> registerAdmin(@Valid @RequestBody AdminSaveRequest request) {
        Admin mappedAdmin = AdminMapper.INSTANCE.adminSaveRequestToModel(request);
        Admin savedAdmin = adminService.save(mappedAdmin);
        return new ResponseEntity<>(AdminMapper.INSTANCE.modelToAdminSaveResponse(savedAdmin), HttpStatus.CREATED);
    }

    @GetMapping("/admin-search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AdminSearchingResponse> adminSearch(@RequestBody AdminSearchingRequest request) {
        return new ResponseEntity<>(new AdminSearchingResponse(adminService.adminSearching(request)), HttpStatus.FOUND);
    }

}