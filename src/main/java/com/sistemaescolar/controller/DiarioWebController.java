package com.sistemaescolar.controller;

import com.sistemaescolar.dto.FrequenciaRequestDTO;
import com.sistemaescolar.dto.FrequenciaResponseDTO;
import com.sistemaescolar.dto.RelatorioFrequenciaAlunoDTO;
import com.sistemaescolar.model.Frequencia;
import com.sistemaescolar.model.Matricula;
import com.sistemaescolar.model.ProfessorTurmaDisciplina;
import com.sistemaescolar.repository.AlunoRepository;
import com.sistemaescolar.repository.FrequenciaRepository;
import com.sistemaescolar.repository.MatriculaRepository;
import com.sistemaescolar.repository.ProfessorTurmaDisciplinaRepository;
import com.sistemaescolar.repository.TurmaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DiarioWebController {

    private final ProfessorTurmaDisciplinaRepository professorTurmaDisciplinaRepository;
    private final MatriculaRepository matriculaRepository;
    private final FrequenciaRepository frequenciaRepository;
    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;

    public DiarioWebController(ProfessorTurmaDisciplinaRepository professorTurmaDisciplinaRepository,
                               MatriculaRepository matriculaRepository,
                               FrequenciaRepository frequenciaRepository,
                               TurmaRepository turmaRepository,
                               AlunoRepository alunoRepository) {
        this.professorTurmaDisciplinaRepository = professorTurmaDisciplinaRepository;
        this.matriculaRepository = matriculaRepository;
        this.frequenciaRepository = frequenciaRepository;
        this.turmaRepository = turmaRepository;
        this.alunoRepository = alunoRepository;
    }

    @GetMapping("/diario")
    public String telaInicial(Model model,
                              @RequestParam(value = "sucesso", required = false) String sucesso,
                              @RequestParam(value = "erro", required = false) String erro,
                              @RequestParam(value = "erroDuplicidade", required = false) String erroDuplicidade) {

        model.addAttribute("vinculos", professorTurmaDisciplinaRepository.findAll());

        if (sucesso != null) {
            model.addAttribute("mensagemSucesso", "Frequência lançada com sucesso.");
        }

        if (erro != null) {
            model.addAttribute("mensagemErro", "Não foi possível concluir a operação.");
        }

        if (erroDuplicidade != null) {
            model.addAttribute("mensagemErro",
                    "Já existe frequência lançada para pelo menos um aluno nessa turma, vínculo e data.");
        }

        return "diario";
    }

    @GetMapping("/diario/frequencia")
    public String telaFrequencia(@RequestParam("vinculoId") Long vinculoId,
                                 @RequestParam("dataAula") String dataAula,
                                 Model model) {

        ProfessorTurmaDisciplina vinculo = professorTurmaDisciplinaRepository.findById(vinculoId).orElse(null);

        if (vinculo == null) {
            return "redirect:/diario?erro=true";
        }

        List<Matricula> matriculas = matriculaRepository.findByTurma_Id(vinculo.getTurma().getId());

        model.addAttribute("vinculo", vinculo);
        model.addAttribute("matriculas", matriculas);
        model.addAttribute("dataAula", dataAula);

        return "diario-frequencia";
    }

    @PostMapping("/diario/frequencia/salvar")
    public String salvarFrequencia(
            @RequestParam("vinculoId") Long vinculoId,
            @RequestParam("dataAula") String dataAula,
            @RequestParam("matriculaIds") List<Long> matriculaIds,
            @RequestParam("presencas") List<String> presencas,
            @RequestParam(value = "observacoes", required = false) List<String> observacoes
    ) {
        try {
            ProfessorTurmaDisciplina vinculo = professorTurmaDisciplinaRepository.findById(vinculoId).orElse(null);

            if (vinculo == null) {
                return "redirect:/diario?erro=true";
            }

            LocalDate data = LocalDate.parse(dataAula);

            for (Long matriculaId : matriculaIds) {
                boolean jaExiste = frequenciaRepository
                        .existsByMatricula_IdAndProfessorTurmaDisciplina_IdAndDataAula(
                                matriculaId, vinculoId, data
                        );

                if (jaExiste) {
                    return "redirect:/diario?erroDuplicidade=true";
                }
            }

            for (int i = 0; i < matriculaIds.size(); i++) {
                Matricula matricula = matriculaRepository.findById(matriculaIds.get(i)).orElse(null);

                if (matricula != null) {
                    Frequencia frequencia = new Frequencia();
                    frequencia.setMatricula(matricula);
                    frequencia.setProfessorTurmaDisciplina(vinculo);
                    frequencia.setDataAula(data);
                    frequencia.setPresenca(presencas.get(i));

                    if (observacoes != null && observacoes.size() > i) {
                        frequencia.setObservacao(observacoes.get(i));
                    }

                    frequenciaRepository.save(frequencia);
                }
            }

            return "redirect:/diario/frequencias?sucesso=true";

        } catch (Exception e) {
            return "redirect:/diario?erro=true";
        }
    }

    @GetMapping("/diario/frequencias")
        public String listarFrequencias(Model model,
                                        @RequestParam(value = "sucesso", required = false) String sucesso,
                                        @RequestParam(value = "editado", required = false) String editado,
                                        @RequestParam(value = "erro", required = false) String erro,
                                        @RequestParam(value = "turmaId", required = false) Long turmaId,
                                        @RequestParam(value = "dataAula", required = false) String dataAula) {

            List<Frequencia> frequencias;

            boolean temTurma = turmaId != null;
            boolean temData = dataAula != null && !dataAula.isBlank();

            if (temTurma && temData) {
                frequencias = frequenciaRepository.findByProfessorTurmaDisciplina_Turma_IdAndDataAula(
                        turmaId, LocalDate.parse(dataAula)
                );
            } else if (temTurma) {
                frequencias = frequenciaRepository.findByProfessorTurmaDisciplina_Turma_Id(turmaId);
            } else if (temData) {
                frequencias = frequenciaRepository.findByDataAula(LocalDate.parse(dataAula));
            } else {
                frequencias = frequenciaRepository.findAll();
            }

            List<FrequenciaResponseDTO> lista = frequencias.stream()
                    .map(f -> new FrequenciaResponseDTO(
                            f.getId(),
                            f.getMatricula().getAluno().getNome(),
                            f.getProfessorTurmaDisciplina().getTurma().getNome(),
                            f.getProfessorTurmaDisciplina().getDisciplina().getNome(),
                            f.getDataAula() != null ? f.getDataAula().toString() : "",
                            f.getPresenca(),
                            f.getObservacao()
                    ))
                    .toList();

            model.addAttribute("frequencias", lista);
            model.addAttribute("turmas", turmaRepository.findAll());
            model.addAttribute("turmaId", turmaId);
            model.addAttribute("dataAula", dataAula);

            if (sucesso != null) {
                model.addAttribute("mensagemSucesso", "Frequência lançada com sucesso.");
            }

            if (editado != null) {
                model.addAttribute("mensagemSucesso", "Frequência atualizada com sucesso.");
            }

            if (erro != null) {
                model.addAttribute("mensagemErro", "Não foi possível concluir a operação.");
            }

            return "diario-frequencias";
        }

    @GetMapping("/diario/frequencia/editar/{id}")
        public String editarFrequencia(@PathVariable Long id, Model model) {
            Frequencia frequencia = frequenciaRepository.findById(id).orElse(null);

            if (frequencia == null) {
                return "redirect:/diario/frequencias?erro=true";
            }

            FrequenciaRequestDTO dto = new FrequenciaRequestDTO();
            dto.setId(frequencia.getId());
            dto.setMatriculaId(frequencia.getMatricula().getId());
            dto.setProfessorTurmaDisciplinaId(frequencia.getProfessorTurmaDisciplina().getId());
            dto.setDataAula(frequencia.getDataAula() != null ? frequencia.getDataAula().toString() : "");
            dto.setPresenca(frequencia.getPresenca());
            dto.setObservacao(frequencia.getObservacao());

            model.addAttribute("frequencia", dto);
            model.addAttribute("matriculas", matriculaRepository.findAll());
            model.addAttribute("vinculos", professorTurmaDisciplinaRepository.findAll());

            return "diario-frequencia-editar";
        }

    @PostMapping("/diario/frequencia/atualizar")
        public String atualizarFrequencia(@ModelAttribute("frequencia") FrequenciaRequestDTO dto) {
            try {
                Frequencia frequencia = frequenciaRepository.findById(dto.getId()).orElse(null);

                if (frequencia == null) {
                    return "redirect:/diario/frequencias?erro=true";
                }

                Matricula matricula = matriculaRepository.findById(dto.getMatriculaId()).orElse(null);
                ProfessorTurmaDisciplina vinculo = professorTurmaDisciplinaRepository
                        .findById(dto.getProfessorTurmaDisciplinaId())
                        .orElse(null);

                if (matricula == null || vinculo == null) {
                    return "redirect:/diario/frequencias?erro=true";
                }

                frequencia.setMatricula(matricula);
                frequencia.setProfessorTurmaDisciplina(vinculo);
                frequencia.setDataAula(LocalDate.parse(dto.getDataAula()));
                frequencia.setPresenca(dto.getPresenca());
                frequencia.setObservacao(dto.getObservacao());

                frequenciaRepository.save(frequencia);

                return "redirect:/diario/frequencias?editado=true";

            } catch (Exception e) {
                return "redirect:/diario/frequencias?erro=true";
            }
        }

    @GetMapping("/diario/relatorio-aluno")
        public String relatorioAluno(@RequestParam(value = "alunoId", required = false) Long alunoId,
                                    Model model) {

            model.addAttribute("alunos", alunoRepository.findAll());

            if (alunoId != null) {
                List<Frequencia> frequencias = frequenciaRepository.findByMatricula_Aluno_Id(alunoId);

                long totalAulas = frequencias.size();

                long totalPresencas = frequencias.stream()
                        .filter(f -> "PRESENTE".equalsIgnoreCase(f.getPresenca()))
                        .count();

                long totalAusencias = frequencias.stream()
                        .filter(f -> "AUSENTE".equalsIgnoreCase(f.getPresenca()))
                        .count();

                double percentualPresenca = totalAulas > 0
                        ? (totalPresencas * 100.0) / totalAulas
                        : 0.0;

                String nomeAluno = alunoRepository.findById(alunoId)
                        .map(a -> a.getNome())
                        .orElse("Aluno");

                RelatorioFrequenciaAlunoDTO resumo = new RelatorioFrequenciaAlunoDTO(
                        nomeAluno,
                        totalAulas,
                        totalPresencas,
                        totalAusencias,
                        percentualPresenca
                );

                List<FrequenciaResponseDTO> historico = frequencias.stream()
                        .map(f -> new FrequenciaResponseDTO(
                                f.getId(),
                                f.getMatricula().getAluno().getNome(),
                                f.getProfessorTurmaDisciplina().getTurma().getNome(),
                                f.getProfessorTurmaDisciplina().getDisciplina().getNome(),
                                f.getDataAula() != null ? f.getDataAula().toString() : "",
                                f.getPresenca(),
                                f.getObservacao()
                        ))
                        .toList();

                model.addAttribute("resumo", resumo);
                model.addAttribute("historico", historico);
                model.addAttribute("alunoId", alunoId);
            }

            return "diario-relatorio-aluno";
        }
}