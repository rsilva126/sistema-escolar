package com.sistemaescolar.dto;

public class RelatorioFrequenciaAlunoDTO {

    private String aluno;
    private long totalAulas;
    private long totalPresencas;
    private long totalAusencias;
    private double percentualPresenca;

    public RelatorioFrequenciaAlunoDTO(String aluno,
                                       long totalAulas,
                                       long totalPresencas,
                                       long totalAusencias,
                                       double percentualPresenca) {
        this.aluno = aluno;
        this.totalAulas = totalAulas;
        this.totalPresencas = totalPresencas;
        this.totalAusencias = totalAusencias;
        this.percentualPresenca = percentualPresenca;
    }

    public String getAluno() {
        return aluno;
    }

    public long getTotalAulas() {
        return totalAulas;
    }

    public long getTotalPresencas() {
        return totalPresencas;
    }

    public long getTotalAusencias() {
        return totalAusencias;
    }

    public double getPercentualPresenca() {
        return percentualPresenca;
    }
}