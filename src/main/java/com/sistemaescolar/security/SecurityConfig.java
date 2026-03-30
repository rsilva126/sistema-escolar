package com.sistemaescolar.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/img/**", "/login").permitAll()

                        .requestMatchers("/usuarios-view/**", "/usuarios/**")
                        .hasAnyRole("ADMINISTRADOR", "DIRETOR")

                        .requestMatchers("/escolas-view/**", "/escolas/**")
                        .hasAnyRole("ADMINISTRADOR", "DIRETOR", "SECRETARIO")

                        .requestMatchers("/alunos-view/**", "/alunos/**")
                        .hasAnyRole("ADMINISTRADOR", "SECRETARIO", "COORDENADOR", "DIRETOR")

                        .requestMatchers("/turmas-view/**", "/turmas/**")
                        .hasAnyRole("ADMINISTRADOR", "SECRETARIO", "COORDENADOR", "DIRETOR")

                        .requestMatchers("/matriculas-view/**", "/matriculas/**")
                        .hasAnyRole("ADMINISTRADOR", "SECRETARIO", "COORDENADOR", "DIRETOR")

                        .requestMatchers("/professor-turma-disciplina-view/**", "/professor-turma-disciplina/**")
                        .hasAnyRole("ADMINISTRADOR", "COORDENADOR", "DIRETOR")

                        .requestMatchers("/diario/**")
                        .hasAnyRole("ADMINISTRADOR", "PROFESSOR", "COORDENADOR", "DIRETOR")

                        .requestMatchers("/")
                        .authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acesso-negado")
                );

        return http.build();
    }
}