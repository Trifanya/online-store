package ru.devtrifanya.online_store.security.jwt;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
