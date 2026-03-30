package com.sistemaescolar.dto;

public class MatriculaRequestDTO {
    private Long id;
    private Long alunoId;
    private Long turmaId;
    private Long anoLetivoId;
    private String dataMatricula;
    private String situacao;
    private String numeroMatricula;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAlunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }
    public Long getTurmaId() { return turmaId; }
    public void setTurmaId(Long turmaId) { this.turmaId = turmaId; }
    public Long getAnoLetivoId() { return anoLetivoId; }
    public void setAnoLetivoId(Long anoLetivoId) { this.anoLetivoId = anoLetivoId; }
    public String getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(String dataMatricula) { this.dataMatricula = dataMatricula; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public String getNumeroMatricula() { return numeroMatricula; }
    public void setNumeroMatricula(String numeroMatricula) { this.numeroMatricula = numeroMatricula; }
}
