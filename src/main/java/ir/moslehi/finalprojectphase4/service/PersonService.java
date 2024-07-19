package ir.moslehi.finalprojectphase4.service;

import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.model.Person;
import ir.moslehi.finalprojectphase4.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("the email : " + email + " wasn't found")
        );
    }

}
