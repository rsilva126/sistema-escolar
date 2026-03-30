package com.sistemaescolar.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "frequencias")
public class Frequencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula_id")
    private Matricula matricula;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professor_turma_disciplina_id")
    private ProfessorTurmaDisciplina professorTurmaDisciplina;

    @Column(name = "data_aula", nullable = false)
    private LocalDate dataAula;

    @Column(nullable = false)
    private String presenca;

    private String observacao;

    public Frequencia() {
    }

    public Long getId() {
        return id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public ProfessorTurmaDisciplina getProfessorTurmaDisciplina() {
        return professorTurmaDisciplina;
    }

    public LocalDate getDataAula() {
        return dataAula;
    }

    public String getPresenca() {
        return presenca;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public void setProfessorTurmaDisciplina(ProfessorTurmaDisciplina professorTurmaDisciplina) {
        this.professorTurmaDisciplina = professorTurmaDisciplina;
    }

    public void setDataAula(LocalDate dataAula) {
        this.dataAula = dataAula;
    }

    public void setPresenca(String presenca) {
        this.presenca = presenca;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}