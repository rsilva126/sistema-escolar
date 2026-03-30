package com.sistemaescolar.repository;

import com.sistemaescolar.model.Frequencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {

    List<Frequencia> findByProfessorTurmaDisciplina_Id(Long vinculoId);

    List<Frequencia> findByProfessorTurmaDisciplina_Turma_Id(Long turmaId);

    List<Frequencia> findByProfessorTurmaDisciplina_Turma_IdAndDataAula(Long turmaId, LocalDate dataAula);

    List<Frequencia> findByMatricula_Aluno_Id(Long alunoId);

    List<Frequencia> findByDataAula(LocalDate dataAula);

    boolean existsByMatricula_IdAndProfessorTurmaDisciplina_IdAndDataAula(
            Long matriculaId,
            Long professorTurmaDisciplinaId,
            LocalDate dataAula
    );
}