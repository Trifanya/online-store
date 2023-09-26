package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.security.jwt.JwtRequest;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.util.exceptions.person.PersonNotFoundException;

import java.util.Optional;

@Service
@Data
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTUtils JWTUtils;

    @Transactional
    public void signUpPerson(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    public String createAuthToken(JwtRequest authRequest) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        Optional<User> person = userRepository.findByEmail(authRequest.getEmail());

        if (person.isEmpty()) {
            throw new PersonNotFoundException("Пользователь с указанным email не найден.");
        }
        return JWTUtils.generateToken(person.get());
    }
}
