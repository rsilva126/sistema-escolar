package com.sistemaescolar.controller;

import com.sistemaescolar.dto.TurmaRequestDTO;
import com.sistemaescolar.dto.TurmaResponseDTO;
import com.sistemaescolar.model.AnoLetivo;
import com.sistemaescolar.model.Escola;
import com.sistemaescolar.model.Serie;
import com.sistemaescolar.model.Turma;
import com.sistemaescolar.model.Turno;
import com.sistemaescolar.repository.AnoLetivoRepository;
import com.sistemaescolar.repository.EscolaRepository;
import com.sistemaescolar.repository.SerieRepository;
import com.sistemaescolar.repository.TurmaRepository;
import com.sistemaescolar.repository.TurnoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TurmaWebController {

    private final TurmaRepository turmaRepository;
    private final SerieRepository serieRepository;
    private final TurnoRepository turnoRepository;
    private final AnoLetivoRepository anoLetivoRepository;
    private final EscolaRepository escolaRepository;

    public TurmaWebController(TurmaRepository turmaRepository,
                              SerieRepository serieRepository,
                              TurnoRepository turnoRepository,
                              AnoLetivoRepository anoLetivoRepository,
                              EscolaRepository escolaRepository) {
        this.turmaRepository = turmaRepository;
        this.serieRepository = serieRepository;
        this.turnoRepository = turnoRepository;
        this.anoLetivoRepository = anoLetivoRepository;
        this.escolaRepository = escolaRepository;
    }

    @GetMapping("/turmas-view")
    public String listarTurmas(
            Model model,
            @RequestParam(value = "sucesso", required = false) String sucesso,
            @RequestParam(value = "excluido", required = false) String excluido,
            @RequestParam(value = "editado", required = false) String editado,
            @RequestParam(value = "erro", required = false) String erro,
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "escolaId", required = false) Long escolaId
    ) {

        List<Turma> turmas;

        boolean temBusca = busca != null && !busca.isBlank();
        boolean temEscola = escolaId != null;

        if (temBusca && temEscola) {
            turmas = turmaRepository.findByNomeContainingIgnoreCaseAndEscola_Id(busca, escolaId);
        } else if (temBusca) {
            turmas = turmaRepository.findByNomeContainingIgnoreCase(busca);
        } else if (temEscola) {
            turmas = turmaRepository.findByEscola_Id(escolaId);
        } else {
            turmas = turmaRepository.findAll();
        }

        List<TurmaResponseDTO> lista = turmas.stream()
                .map(t -> new TurmaResponseDTO(
                        t.getId(),
                        t.getNome(),
                        t.getSerie().getNome(),
                        t.getTurno().getNome(),
                        t.getAnoLetivo().getDescricao(),
                        t.getEscola().getNome(),
                        t.getStatus()
                ))
                .toList();

        model.addAttribute("turmas", lista);
        model.addAttribute("busca", busca);
        model.addAttribute("escolaId", escolaId);
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("totalItens", lista.size());

        if (escolaId != null) {
            escolaRepository.findById(escolaId)
                    .ifPresent(escola -> model.addAttribute("nomeEscolaFiltro", escola.getNome()));
        }

        if (sucesso != null) {
            model.addAttribute("mensagemSucesso", "Turma cadastrada com sucesso.");
        }

        if (excluido != null) {
            model.addAttribute("mensagemSucesso", "Turma excluída com sucesso.");
        }

        if (editado != null) {
            model.addAttribute("mensagemSucesso", "Turma atualizada com sucesso.");
        }

        if (erro != null) {
            model.addAttribute("mensagemErro", "Não foi possível concluir a operação.");
        }

        return "turmas";
    }

    @GetMapping("/turmas/novo")
    public String novaTurma(Model model) {
        model.addAttribute("turma", new TurmaRequestDTO());
        model.addAttribute("series", serieRepository.findAll());
        model.addAttribute("turnos", turnoRepository.findAll());
        model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("modoEdicao", false);
        return "turma-form";
    }

    @GetMapping("/turmas/editar/{id}")
    public String editarTurma(@PathVariable Long id, Model model) {
        Turma turma = turmaRepository.findById(id).orElse(null);

        if (turma == null) {
            return "redirect:/turmas-view?erro=true";
        }

        TurmaRequestDTO dto = new TurmaRequestDTO();
        dto.setId(turma.getId());
        dto.setNome(turma.getNome());
        dto.setSerieId(turma.getSerie().getId());
        dto.setTurnoId(turma.getTurno().getId());
        dto.setAnoLetivoId(turma.getAnoLetivo().getId());
        dto.setEscolaId(turma.getEscola().getId());
        dto.setStatus(turma.getStatus());

        model.addAttribute("turma", dto);
        model.addAttribute("series", serieRepository.findAll());
        model.addAttribute("turnos", turnoRepository.findAll());
        model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("modoEdicao", true);

        return "turma-form";
    }

    @PostMapping("/turmas/salvar")
    public String salvarTurma(@ModelAttribute("turma") TurmaRequestDTO dto, Model model) {

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            model.addAttribute("erro", "O nome da turma é obrigatório.");
            preencherCombos(model);
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "turma-form";
        }

        if (dto.getSerieId() == null || dto.getTurnoId() == null || dto.getAnoLetivoId() == null || dto.getEscolaId() == null) {
            model.addAttribute("erro", "Série, turno, ano letivo e escola são obrigatórios.");
            preencherCombos(model);
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "turma-form";
        }

        Serie serie = serieRepository.findById(dto.getSerieId()).orElse(null);
        Turno turno = turnoRepository.findById(dto.getTurnoId()).orElse(null);
        AnoLetivo anoLetivo = anoLetivoRepository.findById(dto.getAnoLetivoId()).orElse(null);
        Escola escola = escolaRepository.findById(dto.getEscolaId()).orElse(null);

        if (serie == null || turno == null || anoLetivo == null || escola == null) {
            model.addAttribute("erro", "Série, turno, ano letivo ou escola não encontrados.");
            preencherCombos(model);
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "turma-form";
        }

        try {
            Turma turma;

            if (dto.getId() != null) {
                turma = turmaRepository.findById(dto.getId()).orElse(null);
                if (turma == null) {
                    return "redirect:/turmas-view?erro=true";
                }
            } else {
                turma = new Turma();
            }

            turma.setNome(dto.getNome());
            turma.setSerie(serie);
            turma.setTurno(turno);
            turma.setAnoLetivo(anoLetivo);
            turma.setEscola(escola);
            turma.setStatus(dto.getStatus() != null && !dto.getStatus().isBlank() ? dto.getStatus() : "ATIVA");

            turmaRepository.save(turma);

            if (dto.getId() != null) {
                return "redirect:/turmas-view?editado=true";
            }

            return "redirect:/turmas-view?sucesso=true";

        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível salvar a turma.");
            preencherCombos(model);
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "turma-form";
        }
    }

    @GetMapping("/turmas/excluir/{id}")
    public String excluirTurma(@PathVariable Long id) {
        try {
            turmaRepository.deleteById(id);
            return "redirect:/turmas-view?excluido=true";
        } catch (Exception e) {
            return "redirect:/turmas-view?erro=true";
        }
    }

    private void preencherCombos(Model model) {
        model.addAttribute("series", serieRepository.findAll());
        model.addAttribute("turnos", turnoRepository.findAll());
        model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
        model.addAttribute("escolas", escolaRepository.findAll());
    }
}