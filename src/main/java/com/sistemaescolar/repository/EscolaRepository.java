package com.sistemaescolar.repository;

import com.sistemaescolar.model.Escola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
    List<Escola> findByNomeContainingIgnoreCase(String nome);
}