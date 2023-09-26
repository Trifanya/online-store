package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.UserDTO;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.services.AuthenticationService;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.util.errorResponses.UserErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.person.InvalidPersonDataException;
import ru.devtrifanya.online_store.util.exceptions.person.PersonNotFoundException;
import ru.devtrifanya.online_store.util.validators.UserValidator;

import java.util.List;

@RestController
@RequestMapping("/people")
@Data
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final AuthenticationService authenticationService;

    /*@GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        Optional<Person> person = peopleService.findOne(id);
        if (person.isEmpty()) {
            throw new PersonNotFoundException("Пользователь с такими данными не найден.");
        }
        return convertToPersonDTO(person.get());
    }*/

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> edit(@RequestBody @Valid UserDTO userDTO,
                                           @PathVariable("id") int id,
                                           BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage
                        .append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new InvalidPersonDataException(errorMessage.toString());
        }
        userService.update(id, convertToPerson(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    public User convertToPerson(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertToPersonDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(PersonNotFoundException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(InvalidPersonDataException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
