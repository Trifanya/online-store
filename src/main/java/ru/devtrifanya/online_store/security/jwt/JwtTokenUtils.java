package ru.devtrifanya.online_store.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.security.PersonDetails;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    /**
     * В данном методе происходит генерация JWT-токена по данным пользователя.
     */
    public String generateToken(PersonDetails personDetails) {
        Map<String, Object> claims = new HashMap<>(); // компоненты payload в jwt-токене

        List<String> rolesList = personDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        /** Настройка полей класса, входящих в JWT-токен. */
        claims.put("roles", rolesList);
        claims.put("email", personDetails.getPerson().getEmail());

        /** Настройка времени действия выданного JWT-токена. */
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(personDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /** Извлечение email пользователя из JWT-токена. */
    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /** Извлечение ролей пользователя из JWT-токена. */
    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }
}
