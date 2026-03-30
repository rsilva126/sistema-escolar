package com.sistemaescolar.security;

import com.sistemaescolar.model.Usuario;
import com.sistemaescolar.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        String role = usuario.getFuncao() != null && !usuario.getFuncao().isBlank()
                ? usuario.getFuncao().trim().toUpperCase()
                : "PROFESSOR";

        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                "ATIVO".equalsIgnoreCase(usuario.getStatus()),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}