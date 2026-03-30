package com.sistemaescolar.dto;

public record TurmaResponseDTO(
        Long id,
        String nome,
        String serie,
        String turno,
        String anoLetivo,
        String escola,
        String status
) {}
