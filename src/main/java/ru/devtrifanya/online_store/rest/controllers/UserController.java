package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;
import ru.devtrifanya.online_store.rest.validators.UserValidator;
import ru.devtrifanya.online_store.services.implementations.UserService;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final UserValidator userValidator;

    private final MainClassConverter converter;

    @PatchMapping("/updateUserInfo")
    public ResponseEntity<UserDTO> updateUserInfo(@RequestBody @Valid UserDTO userDTO,
                                                 @AuthenticationPrincipal User user) {
        userValidator.validate(userDTO);

        User updatedUser = userService.updateUserInfo(user.getId(), converter.convertToUser(userDTO));

        return ResponseEntity.ok(converter.convertToUserDTO(updatedUser));
    }
}