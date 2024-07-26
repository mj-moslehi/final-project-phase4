package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.model.ExpertSubService;
import ir.moslehi.finalprojectphase4.model.SubService;
import ir.moslehi.finalprojectphase4.repository.Expert_SubServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpertSubServiceService {

    private final Expert_SubServiceRepository expert_subServiceRepository;
    private final ExpertService expertService;
    private final SubServiceService subServiceService;

    public ExpertSubService save(ExpertSubService expert_subService) {
        Expert foundExpert = expertService.findByEmail(expert_subService.getExpert().getEmail());
        SubService foundSubService = subServiceService.findByName(expert_subService.getSubService().getName());
        if (expert_subServiceRepository.findByExpertAndSubService(foundExpert, foundSubService).isPresent())
            throw new DuplicateInformationException("the expert id : " + expert_subService.getExpert().getId() +
                    " and subService id : " + expert_subService.getSubService().getId() + " is duplicate");
        expert_subService.setSubService(foundSubService);
        expert_subService.setExpert(foundExpert);
        return expert_subServiceRepository.save(expert_subService);
    }

    public List<SubService> findSubServiceByExpert(Expert expert) {
        if (expert_subServiceRepository.findSubServiceByExpert(expert).isEmpty())
            throw new NotFoundException("there isn't any subService for this expert");
        return expert_subServiceRepository.findSubServiceByExpert(expert);
    }

    public ExpertSubService findByExpertAndSubService(Expert expert, SubService subService) {
        return expert_subServiceRepository.findByExpertAndSubService(expert, subService).orElseThrow(
                () -> new NotFoundException("the expert id : " + expert.getId() +
                        " and subService id : " + subService.getId() + "wasn't found")
        );
    }


    public void removeByExpertAndSubService(ExpertSubService expert_SubService) {
        ExpertSubService expertSubService = findByExpertAndSubService
                (expertService.findByEmail(expert_SubService.getExpert().getEmail())
                        , subServiceService.findByName(expert_SubService.getSubService().getName()));
        expert_subServiceRepository.delete(expertSubService);
    }

}

