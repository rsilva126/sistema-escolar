package com.sistemaescolar.controller;

import com.sistemaescolar.model.Escola;
import com.sistemaescolar.repository.AlunoRepository;
import com.sistemaescolar.repository.EscolaRepository;
import com.sistemaescolar.repository.MatriculaRepository;
import com.sistemaescolar.repository.TurmaRepository;
import com.sistemaescolar.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final EscolaRepository escolaRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final MatriculaRepository matriculaRepository;
    private final UsuarioRepository usuarioRepository;

    public HomeController(
            EscolaRepository escolaRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository,
            MatriculaRepository matriculaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.escolaRepository = escolaRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.matriculaRepository = matriculaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String home(
            Model model,
            @RequestParam(value = "escolaId", required = false) Long escolaId
    ) {
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("escolaId", escolaId);

        if (escolaId != null) {
            Escola escola = escolaRepository.findById(escolaId).orElse(null);

            if (escola != null) {
                model.addAttribute("nomeEscolaSelecionada", escola.getNome());
                model.addAttribute("totalEscolas", 1);
                model.addAttribute("totalAlunos", alunoRepository.countByEscola_Id(escolaId));
                model.addAttribute("totalTurmas", turmaRepository.countByEscola_Id(escolaId));
                model.addAttribute("totalMatriculas", matriculaRepository.countByTurma_Escola_Id(escolaId));
                model.addAttribute("totalUsuarios", usuarioRepository.countByEscola_Id(escolaId));
            } else {
                carregarTotaisGerais(model);
            }
        } else {
            carregarTotaisGerais(model);
        }

        return "index";
    }

    private void carregarTotaisGerais(Model model) {
        model.addAttribute("totalEscolas", escolaRepository.count());
        model.addAttribute("totalAlunos", alunoRepository.count());
        model.addAttribute("totalTurmas", turmaRepository.count());
        model.addAttribute("totalMatriculas", matriculaRepository.count());
        model.addAttribute("totalUsuarios", usuarioRepository.count());
    }
}