package com.sistemaescolar.model;

import jakarta.persistence.*;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(name = "nivel_ensino", length = 50)
    private String nivelEnsino;

    @Column(nullable = false, length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    public Serie() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNivelEnsino() { return nivelEnsino; }
    public void setNivelEnsino(String nivelEnsino) { this.nivelEnsino = nivelEnsino; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Escola getEscola() { return escola; }
    public void setEscola(Escola escola) { this.escola = escola; }
}
