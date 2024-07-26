package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.exception.DuplicateInformationException;
import ir.moslehi.finalprojectphase4.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void findByEmail(String email) {
        if (personRepository.findByEmail(email).isPresent())
            throw new DuplicateInformationException(email + " is duplicated");
    }

}
