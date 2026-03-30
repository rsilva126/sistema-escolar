package com.sistemaescolar.dto;

public class TurmaRequestDTO {
    private Long id;
    private String nome;
    private Long serieId;
    private Long turnoId;
    private Long anoLetivoId;
    private Long escolaId;
    private String status;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Long getSerieId() { return serieId; }
    public void setSerieId(Long serieId) { this.serieId = serieId; }
    public Long getTurnoId() { return turnoId; }
    public void setTurnoId(Long turnoId) { this.turnoId = turnoId; }
    public Long getAnoLetivoId() { return anoLetivoId; }
    public void setAnoLetivoId(Long anoLetivoId) { this.anoLetivoId = anoLetivoId; }
    public Long getEscolaId() { return escolaId; }
    public void setEscolaId(Long escolaId) { this.escolaId = escolaId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
