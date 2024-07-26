package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ir.moslehi.finalprojectphase4.model.Service save(ir.moslehi.finalprojectphase4.model.Service service) {
        if (serviceRepository.findByName(service.getName()).isPresent())
            throw new DuplicateInformationException(service.getName() + " is duplicate");
        return serviceRepository.save(service);
    }

    public ir.moslehi.finalprojectphase4.model.Service findByName(String name) {
        return serviceRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("service with name " + name + " wasn't found"));
    }

}
