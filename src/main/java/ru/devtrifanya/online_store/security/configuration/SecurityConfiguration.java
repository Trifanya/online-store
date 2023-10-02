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
import ru.devtrifanya.online_store.services.UserService;

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
                        //.requestMatchers("/categories/{categoryId}/{itemId}", "/categories/{categoryId}", "/{userId}/cart", "/{userId}/cart/{itemId}", "/{userId}/cart/{cartElementId}/edit/{itemId}", "/{userId}/cart/delete/{cartElementId}", "/reviews/{itemId}", "/reviews/{itemId}/delete/{reviewId}").hasAnyRole("USER", "ADMIN")
                        //.requestMatchers("/profile/edit/{userId}", "/reviews/{itemId}/new/{userId}").hasRole("USER")
                        //.requestMatchers("/categories/{categoryId}/newItem", "/categories/{categoryId}/{itemId}/edit", "/categories/{categoryId}/{itemId}/delete", "/{categoryId}/features/newFeature", "/{categoryId}/features/edit/{featureId}", "/{categoryId}/features/delete/{featureId}", "/categories/{categoryId}/newCategory", "/categories/{categoryId}/edit", "/categories/{categoryId}/delete").hasRole("ADMIN")
                        //.requestMatchers("/registration", "/authentication").permitAll()
                        .anyRequest().permitAll())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
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

