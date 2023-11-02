package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.UserValidator;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateUserRequest;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final UserValidator userValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: ... /registration
     * Регистрация нового пользователя.
     */
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody @Valid AddOrUpdateUserRequest request) {
        userValidator.performNewUserValidation(request);

        userService.createNewUser(converter.convertToUser(request.getUser()));

        return ResponseEntity.ok("Регистрация прошла успешно.");
    }

    /**
     * Адрес: ... /profile/updateUser
     * Обновление данных пользователя, для пользователя и администратора.
     */
    @PatchMapping("/profile/updateUser")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid AddOrUpdateUserRequest request,
                                            @AuthenticationPrincipal User user) {
        userValidator.performUpdatedUserValidation(request.getUser(), user.getId());

        userService.updateUser(user, converter.convertToUser(request.getUser()));

        return ResponseEntity.ok("Данные пользователя успешно изменены.");
    }
}
