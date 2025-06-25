package app.todo.services;

//#region imports
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import app.todo.data.Person;
import app.todo.data.PersonRepository;
//#endregion

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PersonService {

    private final PersonRepository personRepository;

    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void createPerson(String name, String lastName, String dni) {
        if ("fail".equals(name)) {
            throw new RuntimeException("This is for testing the error handler");
        }
        var person = new Person();
        person.setName(name);
        person.setLastName(lastName);
        person.setDni(dni);
        personRepository.saveAndFlush(person);
    }

    public List<Person> list(Pageable pageable) {
        return personRepository.findAllBy(pageable).toList();
    }

    public void deletePerson(Person person) {
        personRepository.delete(person);
    }
}
