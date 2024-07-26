package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.model.Service;
import ir.moslehi.finalprojectphase4.model.SubService;
import ir.moslehi.finalprojectphase4.repository.SubServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class SubServiceService {

    private final SubServiceRepository subServiceRepository;
    private final ServiceService serviceService;

    public SubService save(SubService subService) {
        if (subServiceRepository.findByName(subService.getName()).isPresent())
            throw new DuplicateInformationException(subService.getName() + " is duplicate");
        if (serviceService.findByName(subService.getService().getName()) == null)
            throw new NotFoundException("service wasn't found");
        else subService.setService(serviceService.findByName(subService.getService().getName()));
        return subServiceRepository.save(subService);
    }

    public List<SubService> findByService(Service service) {
        if (subServiceRepository.findByService(service).isEmpty())
            throw new NotFoundException("there isn't any subService for this service");
        return subServiceRepository.findByService(service);
    }

    public SubService findByName(String name) {
        return subServiceRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("subService with name : " + name + " wasn't found")
        );
    }

    public SubService findSubSerBaseOnSer(String serviceName, String subServiceName) {
        List<SubService> subServices = findByService(serviceService.findByName(serviceName));
        if (!subServices.contains(findByName(subServiceName)))
            throw new NotFoundException("the subService wasn't found");
        return findByName(subServiceName);
    }

    public SubService update(SubService subService) {
        SubService foundSubService = findByName(subService.getName());
        foundSubService.setBasePrice(subService.getBasePrice());
        foundSubService.setDescription(subService.getDescription());
        subServiceRepository.save(foundSubService);
        return foundSubService;
    }

}