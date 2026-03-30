package com.sistemaescolar.dto;

public class EscolaResponseDTO {

    private Long id;
    private String nome;
    private String codigoInep;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;
    private String status;

    public EscolaResponseDTO(Long id, String nome, String codigoInep, String cnpj,
                             String endereco, String telefone, String email, String status) {
        this.id = id;
        this.nome = nome;
        this.codigoInep = codigoInep;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigoInep() {
        return codigoInep;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}