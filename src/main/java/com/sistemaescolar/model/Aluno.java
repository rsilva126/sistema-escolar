package com.sistemaescolar.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "alunos")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 14)
    private String cpf;

    @Column(length = 20)
    private String rg;

    @Column(name = "nome_mae", length = 150)
    private String nomeMae;

    @Column(name = "nome_pai", length = 150)
    private String nomePai;

    @Column(name = "telefone_responsavel", length = 20)
    private String telefoneResponsavel;

    @Column(length = 255)
    private String endereco;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(nullable = false, length = 20)
    private String status;

    public Aluno() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getNomeMae() { return nomeMae; }
    public void setNomeMae(String nomeMae) { this.nomeMae = nomeMae; }
    public String getNomePai() { return nomePai; }
    public void setNomePai(String nomePai) { this.nomePai = nomePai; }
    public String getTelefoneResponsavel() { return telefoneResponsavel; }
    public void setTelefoneResponsavel(String telefoneResponsavel) { this.telefoneResponsavel = telefoneResponsavel; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public Escola getEscola() { return escola; }
    public void setEscola(Escola escola) { this.escola = escola; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
