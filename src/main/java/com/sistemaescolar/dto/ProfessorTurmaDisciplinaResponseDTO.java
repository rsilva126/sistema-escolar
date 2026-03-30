package com.sistemaescolar.dto;

public class ProfessorTurmaDisciplinaResponseDTO {

    private Long id;
    private String professor;
    private String turma;
    private String disciplina;
    private String anoLetivo;
    private String status;

    public ProfessorTurmaDisciplinaResponseDTO(Long id, String professor, String turma,
                                               String disciplina, String anoLetivo, String status) {
        this.id = id;
        this.professor = professor;
        this.turma = turma;
        this.disciplina = disciplina;
        this.anoLetivo = anoLetivo;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getProfessor() {
        return professor;
    }

    public String getTurma() {
        return turma;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getAnoLetivo() {
        return anoLetivo;
    }

    public String getStatus() {
        return status;
    }
}