package ru.devtrifanya.online_store.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.devtrifanya.online_store.dto.PersonDTO;
import ru.devtrifanya.online_store.services.PeopleService;
import ru.devtrifanya.online_store.util.exceptions.person.PersonAlreadyExistException;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO personDTO = (PersonDTO) target;

        if (peopleService.findOne(personDTO.getEmail()).isPresent()) {
            throw new PersonAlreadyExistException("Пользователь с таким email уже существует.");
        }
    }
}
