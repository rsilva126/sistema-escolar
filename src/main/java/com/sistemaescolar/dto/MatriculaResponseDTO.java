package com.sistemaescolar.dto;

public record MatriculaResponseDTO(
        Long id,
        String aluno,
        String turma,
        String anoLetivo,
        String dataMatricula,
        String situacao,
        String numeroMatricula
) {}
