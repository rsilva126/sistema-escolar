package com.sistemaescolar.repository;

import com.sistemaescolar.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByAluno_NomeContainingIgnoreCase(String nome);

    List<Matricula> findByTurma_Escola_Id(Long escolaId);

    List<Matricula> findByAluno_NomeContainingIgnoreCaseAndTurma_Escola_Id(String nome, Long escolaId);

    long countByTurma_Escola_Id(Long escolaId);

    List<Matricula> findByTurma_Id(Long turmaId);
}