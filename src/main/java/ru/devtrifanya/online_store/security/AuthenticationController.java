package ru.devtrifanya.online_store.security;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.devtrifanya.online_store.dto.PersonDTO;
import ru.devtrifanya.online_store.models.Person;
import ru.devtrifanya.online_store.security.jwt.JwtRequest;
import ru.devtrifanya.online_store.security.jwt.JwtResponse;
import ru.devtrifanya.online_store.security.jwt.JwtTokenUtils;
import ru.devtrifanya.online_store.services.PeopleService;
import ru.devtrifanya.online_store.util.errorResponses.AuthenticationErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.person.InvalidPersonDataException;
import ru.devtrifanya.online_store.util.validators.PersonValidator;

import java.util.List;

@RestController
public class AuthenticationController {
   private final AuthenticationService authenticationService;
   private final PersonValidator personValidator;
   private final AuthenticationManager authenticationManager;
   private final JwtTokenUtils jwtTokenUtils;
   private final PersonDetailsService personDetailsService;
   private final PeopleService peopleService;
   private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, PersonValidator personValidator, AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils, PersonDetailsService personDetailsService, PeopleService peopleService, ModelMapper modelMapper) {
        this.authenticationService = authenticationService;
        this.personValidator = personValidator;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
        this.personDetailsService = personDetailsService;
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            return new ResponseEntity<>(new AuthenticationErrorResponse("Неверно введен логин или пароль."), HttpStatus.UNAUTHORIZED);
        }

        PersonDetails personDetails = personDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String token = jwtTokenUtils.generateToken(personDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid PersonDTO personDTO,
                                             BindingResult bindingResult) {
        personValidator.validate(personDTO, bindingResult);

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
        peopleService.signUpPerson(convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
