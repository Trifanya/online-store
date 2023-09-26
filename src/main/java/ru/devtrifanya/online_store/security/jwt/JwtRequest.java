package ru.devtrifanya.online_store.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class JwtRequest {
    private String email;
    private String password;
}
