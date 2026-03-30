package com.sistemaescolar.repository;

import com.sistemaescolar.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    List<Turma> findByNomeContainingIgnoreCase(String nome);

    List<Turma> findByEscola_Id(Long escolaId);

    List<Turma> findByNomeContainingIgnoreCaseAndEscola_Id(String nome, Long escolaId);

    long countByEscola_Id(Long escolaId);
}