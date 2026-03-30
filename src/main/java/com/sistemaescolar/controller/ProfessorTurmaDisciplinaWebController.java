package com.sistemaescolar.controller;

import com.sistemaescolar.dto.ProfessorTurmaDisciplinaRequestDTO;
import com.sistemaescolar.dto.ProfessorTurmaDisciplinaResponseDTO;
import com.sistemaescolar.model.*;
import com.sistemaescolar.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProfessorTurmaDisciplinaWebController {

    private final ProfessorTurmaDisciplinaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AnoLetivoRepository anoLetivoRepository;

    public ProfessorTurmaDisciplinaWebController(
            ProfessorTurmaDisciplinaRepository repository,
            UsuarioRepository usuarioRepository,
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            AnoLetivoRepository anoLetivoRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.turmaRepository = turmaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.anoLetivoRepository = anoLetivoRepository;
    }

    @GetMapping("/professor-turma-disciplina-view")
    public String listar(Model model,
                         @RequestParam(value = "sucesso", required = false) String sucesso,
                         @RequestParam(value = "editado", required = false) String editado,
                         @RequestParam(value = "excluido", required = false) String excluido,
                         @RequestParam(value = "erro", required = false) String erro) {

        List<ProfessorTurmaDisciplinaResponseDTO> lista = repository.findAll().stream()
                .map(item -> new ProfessorTurmaDisciplinaResponseDTO(
                        item.getId(),
                        item.getUsuario().getNome(),
                        item.getTurma().getNome(),
                        item.getDisciplina().getNome(),
                        item.getAnoLetivo().getDescricao(),
                        item.getStatus()
                ))
                .toList();

        model.addAttribute("vinculos", lista);
        model.addAttribute("totalItens", lista.size());

        if (sucesso != null) model.addAttribute("mensagemSucesso", "Vínculo cadastrado com sucesso.");
        if (editado != null) model.addAttribute("mensagemSucesso", "Vínculo atualizado com sucesso.");
        if (excluido != null) model.addAttribute("mensagemSucesso", "Vínculo excluído com sucesso.");
        if (erro != null) model.addAttribute("mensagemErro", "Não foi possível concluir a operação.");

        return "professor-turma-disciplina";
    }

    @GetMapping("/professor-turma-disciplina/novo")
    public String novo(Model model) {
        model.addAttribute("vinculo", new ProfessorTurmaDisciplinaRequestDTO());
        carregarCombos(model);
        model.addAttribute("modoEdicao", false);
        return "professor-turma-disciplina-form";
    }

    @GetMapping("/professor-turma-disciplina/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ProfessorTurmaDisciplina item = repository.findById(id).orElse(null);
        if (item == null) return "redirect:/professor-turma-disciplina-view?erro=true";

        ProfessorTurmaDisciplinaRequestDTO dto = new ProfessorTurmaDisciplinaRequestDTO();
        dto.setId(item.getId());
        dto.setUsuarioId(item.getUsuario().getId());
        dto.setTurmaId(item.getTurma().getId());
        dto.setDisciplinaId(item.getDisciplina().getId());
        dto.setAnoLetivoId(item.getAnoLetivo().getId());
        dto.setStatus(item.getStatus());

        model.addAttribute("vinculo", dto);
        carregarCombos(model);
        model.addAttribute("modoEdicao", true);
        return "professor-turma-disciplina-form";
    }

    @PostMapping("/professor-turma-disciplina/salvar")
    public String salvar(@ModelAttribute("vinculo") ProfessorTurmaDisciplinaRequestDTO dto, Model model) {

        if (dto.getUsuarioId() == null || dto.getTurmaId() == null || dto.getDisciplinaId() == null || dto.getAnoLetivoId() == null) {
            model.addAttribute("erro", "Professor, turma, disciplina e ano letivo são obrigatórios.");
            carregarCombos(model);
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "professor-turma-disciplina-form";
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId()).orElse(null);
        Turma turma = turmaRepository.findById(dto.getTurmaId()).orElse(null);
        Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplinaId()).orElse(null);
        AnoLetivo anoLetivo = anoLetivoRepository.findById(dto.getAnoLetivoId()).orElse(null);

        if (usuario == null || turma == null || disciplina == null || anoLetivo == null) {
            model.addAttribute("erro", "Dados vinculados não encontrados.");
            carregarCombos(model);
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "professor-turma-disciplina-form";
        }

        try {
            ProfessorTurmaDisciplina item;

            if (dto.getId() != null) {
                item = repository.findById(dto.getId()).orElse(null);
                if (item == null) return "redirect:/professor-turma-disciplina-view?erro=true";
            } else {
                item = new ProfessorTurmaDisciplina();
            }

            item.setUsuario(usuario);
            item.setTurma(turma);
            item.setDisciplina(disciplina);
            item.setAnoLetivo(anoLetivo);
            item.setStatus(dto.getStatus() != null && !dto.getStatus().isBlank() ? dto.getStatus() : "ATIVO");

            repository.save(item);

            if (dto.getId() != null) return "redirect:/professor-turma-disciplina-view?editado=true";
            return "redirect:/professor-turma-disciplina-view?sucesso=true";

        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível salvar o vínculo.");
            carregarCombos(model);
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "professor-turma-disciplina-form";
        }
    }

    @GetMapping("/professor-turma-disciplina/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return "redirect:/professor-turma-disciplina-view?excluido=true";
        } catch (Exception e) {
            return "redirect:/professor-turma-disciplina-view?erro=true";
        }
    }

    private void carregarCombos(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("turmas", turmaRepository.findAll());
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("anosLetivos", anoLetivoRepository.findAll());
    }
}