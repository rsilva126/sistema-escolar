package com.sistemaescolar.repository;

import com.sistemaescolar.model.ProfessorTurmaDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorTurmaDisciplinaRepository extends JpaRepository<ProfessorTurmaDisciplina, Long> {
}