package com.learning.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (not needed for REST APIs with JWT)
                .csrf(csrf -> csrf.disable())

                // Disable default login form (we use JWT, not form login)
                .formLogin(form -> form.disable())

                // Disable default basic auth popup (we use JWT, not basic auth)
                .httpBasic(basic -> basic.disable())

                // Define which endpoints are public and which need authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()  // signup & login = public
                        .requestMatchers("/", "/about").permitAll()        // home & about = public
                        .anyRequest().authenticated()                      // everything else = needs token
                )

                // Don't create sessions — JWT is stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Add our JWT filter BEFORE Spring Security's default filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
