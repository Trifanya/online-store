package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;

@Service
@Transactional(readOnly = true)
@Data
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils JWTUtils;

    private final UserRepository userRepository;

    /**
     * Метод получает на вход пользователя (экземпляр класса User), кодирует его пароль
     * с помощью внедренного кодировщика паролей, затем назначает ему роль простого
     * пользователя и сохраняет в БД.
     */
    @Transactional
    public User createNewUser(User userToSave) {
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userToSave.setRole("ROLE_USER");
        return userRepository.save(userToSave);
    }

    /**
     * Менеджер аутентификации проверяет правильность пароля и возвращает результат
     * аутентификации, если пароль верный, в противном случае выбрасывает исключение.
     * Далее из результата аутентификации извлекается Principal (экземпляр класса User)
     * и передается в метод, генерирующий JWT для этого класса.
     */
    public String getJWT(SignInRequest signInRequest) throws BadCredentialsException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                ));
        User user = (User) authentication.getPrincipal();
        return JWTUtils.generateToken(user);
    }
}
