package ru.devtrifanya.online_store.security.configuration;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.devtrifanya.online_store.security.jwt.JwtRequestFilter;
import ru.devtrifanya.online_store.services.implementations.UserService;

@Configuration
@Data
public class SecurityConfiguration {
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/cart", "/reviews/newReview", "/catalog/{categoryId}/{itemId}/newCartElement", "/cart/updateCartElement", "/cart/deleteCartElement"
                        ).hasRole("USER")
                        .requestMatchers(
                                "/reviews/deleteReview", "catalog/{categoryId}/newCategory", "/catalog/{categoryId}/updateCategory", "/catalog/{categoryId}/deleteCategory", "/catalog/{categoryId}/newItem", "/catalog/{categoryId}/{itemId}/updateItem", "/catalog/{categoryId}/{itemId}/deleteItem"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/profile/updateUserInfo"
                        ).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(
                                "/registration", "/authentication", "/catalog", "/catalog/{categoryId}", "/catalog/{categoryId}/{itemId}"
                        ).permitAll())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

