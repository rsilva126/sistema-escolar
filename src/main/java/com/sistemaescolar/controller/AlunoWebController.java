package com.sistemaescolar.controller;

import com.sistemaescolar.dto.AlunoRequestDTO;
import com.sistemaescolar.dto.AlunoResponseDTO;
import com.sistemaescolar.model.Aluno;
import com.sistemaescolar.model.Escola;
import com.sistemaescolar.repository.AlunoRepository;
import com.sistemaescolar.repository.EscolaRepository;
import com.sistemaescolar.repository.TurmaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AlunoWebController {

    private final AlunoRepository alunoRepository;
    private final EscolaRepository escolaRepository;
    private final TurmaRepository turmaRepository;

    public AlunoWebController(AlunoRepository alunoRepository,
                              EscolaRepository escolaRepository,
                              TurmaRepository turmaRepository) {
        this.alunoRepository = alunoRepository;
        this.escolaRepository = escolaRepository;
        this.turmaRepository = turmaRepository;
    }

    @GetMapping("/alunos-view")
    public String listarAlunos(
            Model model,
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "escolaId", required = false) Long escolaId,
            @RequestParam(value = "turmaId", required = false) Long turmaId,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") int tamanho,
            @RequestParam(value = "sucesso", required = false) String sucesso,
            @RequestParam(value = "editado", required = false) String editado,
            @RequestParam(value = "excluido", required = false) String excluido,
            @RequestParam(value = "erro", required = false) String erro
    ) {
        String buscaFiltro = (busca != null && !busca.isBlank()) ? busca : null;
        String statusFiltro = (status != null && !status.isBlank()) ? status : null;

        Pageable pageable = PageRequest.of(pagina, tamanho);

        Page<Aluno> paginaAlunos = alunoRepository.filtrarAlunos(
                buscaFiltro,
                statusFiltro,
                escolaId,
                turmaId,
                pageable
        );

        Page<AlunoResponseDTO> alunos = paginaAlunos.map(aluno -> new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getDataNascimento() != null ? aluno.getDataNascimento().toString() : "",
                aluno.getCpf(),
                aluno.getNomeMae(),
                aluno.getNomePai(),
                aluno.getTelefoneResponsavel(),
                aluno.getEscola() != null ? aluno.getEscola().getNome() : "",
                aluno.getStatus()
        ));

        model.addAttribute("alunos", alunos.getContent());
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("totalPaginas", alunos.getTotalPages());
        model.addAttribute("totalItens", alunos.getTotalElements());

        model.addAttribute("busca", busca);
        model.addAttribute("status", status);
        model.addAttribute("escolaId", escolaId);
        model.addAttribute("turmaId", turmaId);

        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("turmas", turmaRepository.findAll());

        if (escolaId != null) {
            escolaRepository.findById(escolaId)
                    .ifPresent(escola -> model.addAttribute("nomeEscolaFiltro", escola.getNome()));
        }

        if (turmaId != null) {
            turmaRepository.findById(turmaId)
                    .ifPresent(turma -> model.addAttribute("nomeTurmaFiltro", turma.getNome()));
        }

        if (sucesso != null) model.addAttribute("mensagemSucesso", "Aluno cadastrado com sucesso.");
        if (editado != null) model.addAttribute("mensagemSucesso", "Aluno atualizado com sucesso.");
        if (excluido != null) model.addAttribute("mensagemSucesso", "Aluno excluído com sucesso.");
        if (erro != null) model.addAttribute("mensagemErro", "Não foi possível concluir a operação.");

        return "alunos";
    }

    @GetMapping("/alunos/novo")
    public String novoAluno(Model model) {
        model.addAttribute("aluno", new AlunoRequestDTO());
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("modoEdicao", false);
        return "aluno-form";
    }

    @GetMapping("/alunos/editar/{id}")
    public String editarAluno(@PathVariable Long id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElse(null);

        if (aluno == null) {
            return "redirect:/alunos-view?erro=true";
        }

        AlunoRequestDTO dto = new AlunoRequestDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setDataNascimento(aluno.getDataNascimento() != null ? aluno.getDataNascimento().toString() : null);
        dto.setCpf(aluno.getCpf());
        dto.setRg(aluno.getRg());
        dto.setNomeMae(aluno.getNomeMae());
        dto.setNomePai(aluno.getNomePai());
        dto.setTelefoneResponsavel(aluno.getTelefoneResponsavel());
        dto.setEndereco(aluno.getEndereco());
        dto.setEscolaId(aluno.getEscola() != null ? aluno.getEscola().getId() : null);
        dto.setStatus(aluno.getStatus());

        model.addAttribute("aluno", dto);
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("modoEdicao", true);
        return "aluno-form";
    }

    @PostMapping("/alunos/salvar")
    public String salvarAluno(@ModelAttribute("aluno") AlunoRequestDTO dto, Model model) {

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            model.addAttribute("erro", "O nome do aluno é obrigatório.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "aluno-form";
        }

        if (dto.getEscolaId() == null) {
            model.addAttribute("erro", "A escola é obrigatória.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "aluno-form";
        }

        Escola escola = escolaRepository.findById(dto.getEscolaId()).orElse(null);

        if (escola == null) {
            model.addAttribute("erro", "Escola não encontrada.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "aluno-form";
        }

        try {
            Aluno aluno;

            if (dto.getId() != null) {
                aluno = alunoRepository.findById(dto.getId()).orElse(null);
                if (aluno == null) {
                    return "redirect:/alunos-view?erro=true";
                }
            } else {
                aluno = new Aluno();
            }

            aluno.setNome(dto.getNome());

            if (dto.getDataNascimento() != null && !dto.getDataNascimento().isBlank()) {
                aluno.setDataNascimento(java.time.LocalDate.parse(dto.getDataNascimento()));
            } else {
                aluno.setDataNascimento(null);
            }

            aluno.setCpf(dto.getCpf());
            aluno.setRg(dto.getRg());
            aluno.setNomeMae(dto.getNomeMae());
            aluno.setNomePai(dto.getNomePai());
            aluno.setTelefoneResponsavel(dto.getTelefoneResponsavel());
            aluno.setEndereco(dto.getEndereco());
            aluno.setEscola(escola);
            aluno.setStatus(dto.getStatus() != null && !dto.getStatus().isBlank() ? dto.getStatus() : "ATIVO");

            alunoRepository.save(aluno);

            if (dto.getId() != null) {
                return "redirect:/alunos-view?editado=true";
            }

            return "redirect:/alunos-view?sucesso=true";

        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível salvar o aluno.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "aluno-form";
        }
    }

    @GetMapping("/alunos/excluir/{id}")
    public String excluirAluno(@PathVariable Long id) {
        try {
            alunoRepository.deleteById(id);
            return "redirect:/alunos-view?excluido=true";
        } catch (Exception e) {
            return "redirect:/alunos-view?erro=true";
        }
    }
}