package ru.devtrifanya.online_store.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.rest.utils.MainExceptionHandler;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.services.UserService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@Component
@Data
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                String username = jwtUtils.getUsername(jwt);
                User user = userService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                user.getPassword(),
                                user.getAuthorities()
                        );
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Недействительный JWT.");
            }

        } else {
            System.out.println("JWT не был передан.");
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Для доступа к данной странице необходимо авторизоваться.");
        }
        filterChain.doFilter(request, response);
    }
}
