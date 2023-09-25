package ru.devtrifanya.online_store.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.devtrifanya.online_store.security.jwt.JwtRequest;
import ru.devtrifanya.online_store.security.jwt.JwtResponse;
import ru.devtrifanya.online_store.security.jwt.JwtTokenUtils;
import ru.devtrifanya.online_store.util.errorResponses.AuthenticationErrorResponse;

@Service
public class AuthenticationService {
    private final PersonDetailsService personDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(PersonDetailsService personDetailsService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.personDetailsService = personDetailsService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
    }

}
