package com.sistemaescolar.repository;

import com.sistemaescolar.model.AnoLetivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnoLetivoRepository extends JpaRepository<AnoLetivo, Long> {
}