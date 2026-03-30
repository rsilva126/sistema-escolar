package com.sistemaescolar.controller;

import com.sistemaescolar.dto.MatriculaRequestDTO;
import com.sistemaescolar.dto.MatriculaResponseDTO;
import com.sistemaescolar.model.Aluno;
import com.sistemaescolar.model.AnoLetivo;
import com.sistemaescolar.model.Matricula;
import com.sistemaescolar.model.Turma;
import com.sistemaescolar.repository.AlunoRepository;
import com.sistemaescolar.repository.AnoLetivoRepository;
import com.sistemaescolar.repository.EscolaRepository;
import com.sistemaescolar.repository.MatriculaRepository;
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
import java.util.stream.Collectors;

@Controller
public class MatriculaWebController {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final AnoLetivoRepository anoLetivoRepository;
    private final EscolaRepository escolaRepository;

    public MatriculaWebController(MatriculaRepository matriculaRepository,
                                  AlunoRepository alunoRepository,
                                  TurmaRepository turmaRepository,
                                  AnoLetivoRepository anoLetivoRepository,
                                  EscolaRepository escolaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.anoLetivoRepository = anoLetivoRepository;
        this.escolaRepository = escolaRepository;
    }

    @GetMapping("/matriculas-view")
    public String listarMatriculasPagina(
            Model model,
            @RequestParam(value = "sucesso", required = false) String sucesso,
            @RequestParam(value = "excluido", required = false) String excluido,
            @RequestParam(value = "editado", required = false) String editado,
            @RequestParam(value = "erro", required = false) String erro,
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "escolaId", required = false) Long escolaId
    ) {
        List<Matricula> matriculas;

        boolean temBusca = busca != null && !busca.isBlank();
        boolean temEscola = escolaId != null;

        if (temBusca && temEscola) {
            matriculas = matriculaRepository.findByAluno_NomeContainingIgnoreCaseAndTurma_Escola_Id(busca, escolaId);
        } else if (temBusca) {
            matriculas = matriculaRepository.findByAluno_NomeContainingIgnoreCase(busca);
        } else if (temEscola) {
            matriculas = matriculaRepository.findByTurma_Escola_Id(escolaId);
        } else {
            matriculas = matriculaRepository.findAll();
        }

        List<MatriculaResponseDTO> lista = matriculas.stream()
                .map(matricula -> new MatriculaResponseDTO(
                        matricula.getId(),
                        matricula.getAluno().getNome(),
                        matricula.getTurma().getNome(),
                        matricula.getAnoLetivo().getDescricao(),
                        matricula.getDataMatricula() != null ? matricula.getDataMatricula().toString() : null,
                        matricula.getSituacao(),
                        matricula.getNumeroMatricula()
                ))
                .collect(Collectors.toList());

        model.addAttribute("matriculas", lista);
        model.addAttribute("busca", busca);
        model.addAttribute("escolaId", escolaId);
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("totalItens", lista.size());

        if (escolaId != null) {
            escolaRepository.findById(escolaId)
                    .ifPresent(escola -> model.addAttribute("nomeEscolaFiltro", escola.getNome()));
        }

        if (sucesso != null) {
            model.addAttribute("mensagemSucesso", "Matrícula cadastrada com sucesso.");
        }

        if (excluido != null) {
            model.addAttribute("mensagemSucesso", "Matrícula excluída com sucesso.");
        }

        if (editado != null) {
            model.addAttribute("mensagemSucesso", "Matrícula atualizada com sucesso.");
        }

        if (erro != null) {
            model.addAttribute("mensagemErro", "Não foi possível concluir a operação.");
        }

        return "matriculas";
    }

    @GetMapping("/matriculas/novo")
    public String novaMatricula(Model model) {
        model.addAttribute("matricula", new MatriculaRequestDTO());
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("turmas", turmaRepository.findAll());
        model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
        model.addAttribute("modoEdicao", false);
        return "matricula-form";
    }

    @GetMapping("/matriculas/editar/{id}")
    public String editarMatricula(@PathVariable Long id, Model model) {
        Matricula matricula = matriculaRepository.findById(id).orElse(null);

        if (matricula == null) {
            return "redirect:/matriculas-view?erro=true";
        }

        MatriculaRequestDTO dto = new MatriculaRequestDTO();
        dto.setId(matricula.getId());
        dto.setAlunoId(matricula.getAluno().getId());
        dto.setTurmaId(matricula.getTurma().getId());
        dto.setAnoLetivoId(matricula.getAnoLetivo().getId());
        dto.setDataMatricula(matricula.getDataMatricula() != null ? matricula.getDataMatricula().toString() : null);
        dto.setSituacao(matricula.getSituacao());
        dto.setNumeroMatricula(matricula.getNumeroMatricula());

        model.addAttribute("matricula", dto);
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("turmas", turmaRepository.findAll());
        model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
        model.addAttribute("modoEdicao", true);

        return "matricula-form";
    }

    @PostMapping("/matriculas/salvar")
    public String salvarMatricula(@ModelAttribute("matricula") MatriculaRequestDTO dto, Model model) {

        if (dto.getAlunoId() == null || dto.getTurmaId() == null || dto.getAnoLetivoId() == null) {
            model.addAttribute("erro", "Aluno, turma e ano letivo são obrigatórios.");
            model.addAttribute("alunos", alunoRepository.findAll());
            model.addAttribute("turmas", turmaRepository.findAll());
            model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "matricula-form";
        }

        Aluno aluno = alunoRepository.findById(dto.getAlunoId()).orElse(null);
        Turma turma = turmaRepository.findById(dto.getTurmaId()).orElse(null);
        AnoLetivo anoLetivo = anoLetivoRepository.findById(dto.getAnoLetivoId()).orElse(null);

        if (aluno == null || turma == null || anoLetivo == null) {
            model.addAttribute("erro", "Aluno, turma ou ano letivo não encontrados.");
            model.addAttribute("alunos", alunoRepository.findAll());
            model.addAttribute("turmas", turmaRepository.findAll());
            model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "matricula-form";
        }

        try {
            Matricula matricula;

            if (dto.getId() != null) {
                matricula = matriculaRepository.findById(dto.getId()).orElse(null);
                if (matricula == null) {
                    return "redirect:/matriculas-view?erro=true";
                }
            } else {
                matricula = new Matricula();
            }

            matricula.setAluno(aluno);
            matricula.setTurma(turma);
            matricula.setAnoLetivo(anoLetivo);
            matricula.setDataMatricula(
                    dto.getDataMatricula() != null && !dto.getDataMatricula().isBlank()
                            ? LocalDate.parse(dto.getDataMatricula())
                            : null
            );
            matricula.setSituacao(dto.getSituacao());
            matricula.setNumeroMatricula(dto.getNumeroMatricula());

            matriculaRepository.save(matricula);

            if (dto.getId() != null) {
                return "redirect:/matriculas-view?editado=true";
            }

            return "redirect:/matriculas-view?sucesso=true";

        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível salvar a matrícula. Verifique os dados.");
            model.addAttribute("alunos", alunoRepository.findAll());
            model.addAttribute("turmas", turmaRepository.findAll());
            model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "matricula-form";
        }
    }

    @GetMapping("/matriculas/excluir/{id}")
    public String excluirMatricula(@PathVariable Long id) {
        try {
            matriculaRepository.deleteById(id);
            return "redirect:/matriculas-view?excluido=true";
        } catch (Exception e) {
            return "redirect:/matriculas-view?erro=true";
        }
    }
}