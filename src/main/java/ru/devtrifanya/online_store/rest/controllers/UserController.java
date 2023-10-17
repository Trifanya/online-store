package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.rest.dto.requests.UpdateUserRequest;
import ru.devtrifanya.online_store.rest.validators.UserValidator;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;

    private final UserValidator userValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: ... /profile/updateUserInfo
     * Обновление данных пользователя, для пользователя и администратора.
     */
    @PatchMapping("/updateUserInfo")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UpdateUserRequest request,
                                            @AuthenticationPrincipal User user) {
        userValidator.validate(request.getUser(), user.getId());

        userService.updateUserInfo(user, converter.convertToUser(request.getUser()));

        return ResponseEntity.ok("Данные пользователя успешно изменены.");
    }
}
