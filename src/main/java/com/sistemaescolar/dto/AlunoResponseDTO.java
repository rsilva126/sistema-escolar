package com.sistemaescolar.dto;

public record AlunoResponseDTO(
        Long id,
        String nome,
        String dataNascimento,
        String cpf,
        String nomeMae,
        String nomePai,
        String telefoneResponsavel,
        String escola,
        String status
) {}
