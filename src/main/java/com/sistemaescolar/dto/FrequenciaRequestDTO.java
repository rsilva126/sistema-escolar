package com.sistemaescolar.dto;

public class FrequenciaRequestDTO {

    private Long id;
    private Long matriculaId;
    private Long professorTurmaDisciplinaId;
    private String dataAula;
    private String presenca;
    private String observacao;

    public FrequenciaRequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getMatriculaId() {
        return matriculaId;
    }

    public Long getProfessorTurmaDisciplinaId() {
        return professorTurmaDisciplinaId;
    }

    public String getDataAula() {
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

    public void setMatriculaId(Long matriculaId) {
        this.matriculaId = matriculaId;
    }

    public void setProfessorTurmaDisciplinaId(Long professorTurmaDisciplinaId) {
        this.professorTurmaDisciplinaId = professorTurmaDisciplinaId;
    }

    public void setDataAula(String dataAula) {
        this.dataAula = dataAula;
    }

    public void setPresenca(String presenca) {
        this.presenca = presenca;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}