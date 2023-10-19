package ru.devtrifanya.online_store.security.configuration;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.security.jwt.JwtRequestFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/cart", "/catalog/{categoryId}/{itemId}/newReview",
                                "/catalog/{categoryId}/{itemId}/newCartElement", "/cart/updateCartElement", "/cart/deleteCartElement"
                        ).hasRole("USER")
                        .requestMatchers(
                                "/catalog/{categoryId}/newCategory", "/catalog/{categoryId}/updateCategory", "/catalog/{categoryId}/deleteCategory",
                                "/catalog/{categoryId}/newItem", "/catalog/{categoryId}/{itemId}/updateItem", "/catalog/{categoryId}/{itemId}/deleteItem",
                                "/catalog/allFeatures","/catalog/{categoryId}/newFeature", "/catalog/{categoryId}/updateFeature", "/catalog/{categoryId}/deleteFeature",
                                "/catalog/{categoryId}/{itemId}/newReview", "/catalog/{categoryId}/{itemId}/deleteReview"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/profile/updateUserInfo"
                        ).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(
                                "/registration", "/authentication",
                                "/catalog", "/catalog/{categoryId}", "/catalog/{categoryId}/{itemId}"
                        ).permitAll())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

