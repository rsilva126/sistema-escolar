package com.sistemaescolar.dto;

public class FrequenciaResponseDTO {

    private Long id;
    private String aluno;
    private String turma;
    private String disciplina;
    private String dataAula;
    private String presenca;
    private String observacao;

    public FrequenciaResponseDTO(Long id, String aluno, String turma, String disciplina,
                                 String dataAula, String presenca, String observacao) {
        this.id = id;
        this.aluno = aluno;
        this.turma = turma;
        this.disciplina = disciplina;
        this.dataAula = dataAula;
        this.presenca = presenca;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public String getAluno() {
        return aluno;
    }

    public String getTurma() {
        return turma;
    }

    public String getDisciplina() {
        return disciplina;
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
}