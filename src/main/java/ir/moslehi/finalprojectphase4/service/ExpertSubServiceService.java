package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.model.enums.ExpertStatus;
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
        Expert foundExpert = expertService.findById(expert_subService.getExpert().getId());
        if (expert_subServiceRepository.findByExpertAndSubService
                (expert_subService.getExpert(), expert_subService.getSubService()).isPresent())
            throw new DuplicateInformationException("the expert id : " + expert_subService.getExpert().getId() +
                    " and subService id : " + expert_subService.getSubService().getId() + " is duplicate");
        if (!foundExpert.getExpertStatus().equals(ExpertStatus.CONFIRMED) &&
                foundExpert.getScore() < 0)
            throw new NotFoundException("that expert want' found");
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


    public void removeByExpertAndSubService( Long expertId, Long subServiceId) {
        ExpertSubService expertSubService = findByExpertAndSubService
                (expertService.findById(expertId), subServiceService.findById(subServiceId));
        expert_subServiceRepository.delete(expertSubService);
    }

}

