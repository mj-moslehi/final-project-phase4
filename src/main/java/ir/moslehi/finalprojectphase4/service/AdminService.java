package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.dto.customer.CustomerSaveResponse;
import ir.moslehi.finalprojectphase4.dto.expert.ExpertSaveResponse;
import ir.moslehi.finalprojectphase4.dto.orders.OrdersSaveResponse;
import ir.moslehi.finalprojectphase4.dto.search.AdminSearchingRequest;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.mapper.CustomerMapper;
import ir.moslehi.finalprojectphase4.mapper.ExpertMapper;
import ir.moslehi.finalprojectphase4.mapper.OrderMapper;
import ir.moslehi.finalprojectphase4.model.Admin;
import ir.moslehi.finalprojectphase4.model.enums.Role;
import ir.moslehi.finalprojectphase4.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonService personService;
    private final OrdersService ordersService;
    private final CustomerService customerService;
    private final ExpertService expertService;

    public Admin save(Admin admin) {
        personService.findByEmail(admin.getEmail());
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

    public List<Object> adminSearching(AdminSearchingRequest searchingRequest) {
        List<CustomerSaveResponse> customerSaveResponseList = customerService.customerSearch(searchingRequest)
                .stream()
                .map(CustomerMapper.INSTANCE::modelToCustomerSaveResponse)
                .toList();
        List<ExpertSaveResponse> technicianSaveResponseList = expertService.expertSearch(searchingRequest)
                .stream()
                .map(ExpertMapper.INSTANCE::modelToExpertSaveResponse)
                .toList();
        List<OrdersSaveResponse> ordersSaveResponseList = ordersService.ordersSearch(searchingRequest)
                .stream()
                .map(OrderMapper.INSTANCE::modelToOrdersSaveResponse)
                .toList();
        List<Object> list = new ArrayList<>(customerSaveResponseList);
        list.addAll(technicianSaveResponseList);
        list.addAll(ordersSaveResponseList);
        return list;
    }

}
