package com.sistemaescolar.dto;

public class ProfessorTurmaDisciplinaRequestDTO {

    private Long id;
    private Long usuarioId;
    private Long turmaId;
    private Long disciplinaId;
    private Long anoLetivoId;
    private String status;

    public ProfessorTurmaDisciplinaRequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public Long getAnoLetivoId() {
        return anoLetivoId;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public void setAnoLetivoId(Long anoLetivoId) {
        this.anoLetivoId = anoLetivoId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}