package com.sistemaescolar.repository;

import com.sistemaescolar.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    List<Usuario> findByEscola_Id(Long escolaId);

    List<Usuario> findByNomeContainingIgnoreCaseAndEscola_Id(String nome, Long escolaId);

    long countByEscola_Id(Long escolaId);

    Optional<Usuario> findByEmail(String email);
}