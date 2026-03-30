package com.sistemaescolar.dto;

public class UsuarioRequestDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String telefone;
    private Long escolaId;
    private String status;
    private String funcao;

    public UsuarioRequestDTO() {
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

    public String getSenha() {
        return senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public Long getEscolaId() {
        return escolaId;
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

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEscolaId(Long escolaId) {
        this.escolaId = escolaId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}