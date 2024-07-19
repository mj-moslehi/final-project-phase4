package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.model.Admin;
import ir.moslehi.finalprojectphase4.model.enums.Role;
import ir.moslehi.finalprojectphase4.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Admin save(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent())
            throw new DuplicateInformationException(admin.getEmail() + " is duplicate");
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(Role.ROLE_ADMIN);
        admin.setEnabled(true);
        return adminRepository.save(admin);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("the admin with email" + email + "wasn't found")
        );
    }

}
