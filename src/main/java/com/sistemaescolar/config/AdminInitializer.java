package com.sistemaescolar.config;

import com.sistemaescolar.model.Escola;
import com.sistemaescolar.model.Usuario;
import com.sistemaescolar.repository.EscolaRepository;
import com.sistemaescolar.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(
            UsuarioRepository usuarioRepository,
            EscolaRepository escolaRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Escola escola = escolaRepository.findAll().stream().findFirst().orElseGet(() -> {
                Escola novaEscola = new Escola();
                novaEscola.setNome("Escola Padrão");
                novaEscola.setStatus("ATIVA");
                return escolaRepository.save(novaEscola);
            });

            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail("admin@admin.com");

            if (usuarioExistente.isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@admin.com");
                admin.setSenha(passwordEncoder.encode("123456"));
                admin.setStatus("ATIVO");
                admin.setFuncao("ADMINISTRADOR");
                admin.setEscola(escola);

                usuarioRepository.save(admin);
            } else {
                Usuario admin = usuarioExistente.get();
                admin.setSenha(passwordEncoder.encode("123456"));
                admin.setStatus("ATIVO");
                admin.setFuncao("ADMINISTRADOR");
                admin.setEscola(escola);

                usuarioRepository.save(admin);
            }
        };
    }
}