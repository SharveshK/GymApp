package com.gymapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    // We will add a JwtAuthFilter here in the next step

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Cross-Site Request Forgery)
                // We don't need it for a stateless REST API
                .csrf(csrf -> csrf.disable())

                // 2. Define our URL rules
                .authorizeHttpRequests(auth -> auth
                        // All requests to "/api/auth/**" are allowed
                        .requestMatchers("/api/auth/**").permitAll()

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )

                // 3. Set session management to STATELESS
                // This tells Spring not to create HTTP sessions
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Tell Spring Security to use our custom AuthenticationProvider
                .authenticationProvider(authenticationProvider);

        // 5. In the next step, we will add our JWT filter here
        // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}