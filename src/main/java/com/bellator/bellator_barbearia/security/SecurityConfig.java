package com.bellator.bellator_barbearia.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 🔓 LIBERA FRONT (ESSENCIAL)
                .requestMatchers(
                    "/",
                    "/health",
                    "/images/**",
                    "/css/**",
                    "/app",
                    "/index.html",
                    "/styles.css",
                    "/favicon.ico",
                    "/assets/**",
                    "/js/**",
                    "/static/**",
                    "/resources/**",
                    "/calendar.js"
                ).permitAll()

                // 🔓 LIBERA LOGIN / H2 E ERROS E TESTE EMAIL
                .requestMatchers("/auth/**", "/h2-console/**", "/error", "/test-email").permitAll()

                // 🔓 LIBERA LISTAGENS PÚBLICAS (Serviços e Barbeiros)
                .requestMatchers(HttpMethod.GET, "/servicos", "/usuarios/barbeiros").permitAll()

                // 🔓 Horários ocupados na agenda (fluxo de agendamento no front)
                .requestMatchers(HttpMethod.GET, "/agenda").permitAll()

                // 🔒 RESTO PROTEGIDO
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        // JWT imediatamente antes da autorização para o contexto não ser limpo por filtros intermediários
        http.addFilterBefore(jwtAuthFilter, AuthorizationFilter.class);

        // H2 console
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}