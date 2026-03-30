package com.sistemaescolar.dto;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String escola;
    private String status;
    private String funcao;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long id, String nome, String cpf, String email,
                              String telefone, String escola, String status, String funcao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.escola = escola;
        this.status = status;
        this.funcao = funcao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEscola() {
        return escola;
    }

    public String getStatus() {
        return status;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEscola(String escola) {
        this.escola = escola;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}