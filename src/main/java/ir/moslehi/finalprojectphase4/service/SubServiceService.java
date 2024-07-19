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
        if (serviceService.findById(subService.getService().getId()) == null)
            throw new NotFoundException("service wasn't found");
        return subServiceRepository.save(subService);
    }

    public List<SubService> findByService(Service service) {
        if (subServiceRepository.findByService(service).isEmpty())
            throw new NotFoundException("there isn't any subService for this service");
        return subServiceRepository.findByService(service);
    }

    public SubService findById(Long id) {
        return subServiceRepository.findById(id).orElseThrow(
                () -> new NotFoundException("subService with id : " + id + " wasn't found")
        );
    }

    public SubService findByName(String name){
        return subServiceRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("subService with name : " + name + " wasn't found")
        );
    }

    public SubService findSubSerBaseOnSer(Long serviceId, Long subServiceId) {
        List<SubService> subServices = findByService(serviceService.findById(serviceId));
        if (!subServices.contains(findById(subServiceId)))
            throw new NotFoundException("the subService wasn't found");
        return findById(subServiceId);
    }

    public SubService update(SubService subService) {
        SubService foundSubService = findById(subService.getId());
        foundSubService.setBasePrice(subService.getBasePrice());
        foundSubService.setDescription(subService.getDescription());
        subServiceRepository.save(foundSubService);
        return foundSubService;
    }

}