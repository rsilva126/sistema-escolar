package com.sistemaescolar.controller;

import com.sistemaescolar.dto.EscolaRequestDTO;
import com.sistemaescolar.dto.EscolaResponseDTO;
import com.sistemaescolar.model.Escola;
import com.sistemaescolar.repository.EscolaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EscolaWebController {

    private final EscolaRepository escolaRepository;

    public EscolaWebController(EscolaRepository escolaRepository) {
        this.escolaRepository = escolaRepository;
    }

    @GetMapping("/escolas-view")
    public String listarEscolas(
            Model model,
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "sucesso", required = false) String sucesso,
            @RequestParam(value = "editado", required = false) String editado,
            @RequestParam(value = "excluido", required = false) String excluido,
            @RequestParam(value = "erro", required = false) String erro
    ) {
        List<Escola> escolas;

        if (busca != null && !busca.isBlank()) {
            escolas = escolaRepository.findByNomeContainingIgnoreCase(busca);
        } else {
            escolas = escolaRepository.findAll();
        }

        List<EscolaResponseDTO> lista = escolas.stream()
                .map(escola -> new EscolaResponseDTO(
                        escola.getId(),
                        escola.getNome(),
                        escola.getCodigoInep(),
                        escola.getCnpj(),
                        escola.getEndereco(),
                        escola.getTelefone(),
                        escola.getEmail(),
                        escola.getStatus()
                ))
                .collect(Collectors.toList());

        model.addAttribute("escolas", lista);
        model.addAttribute("busca", busca);
        model.addAttribute("totalItens", lista.size());

        if (sucesso != null) model.addAttribute("mensagemSucesso", "Escola cadastrada com sucesso.");
        if (editado != null) model.addAttribute("mensagemSucesso", "Escola atualizada com sucesso.");
        if (excluido != null) model.addAttribute("mensagemSucesso", "Escola excluída com sucesso.");
        if (erro != null) model.addAttribute("mensagemErro", "Não foi possível concluir a operação.");

        return "escolas";
    }

    @GetMapping("/escolas/novo")
    public String novaEscola(Model model) {
        model.addAttribute("escola", new EscolaRequestDTO());
        model.addAttribute("modoEdicao", false);
        return "escola-form";
    }

    @GetMapping("/escolas/editar/{id}")
    public String editarEscola(@PathVariable Long id, Model model) {
        Escola escola = escolaRepository.findById(id).orElse(null);

        if (escola == null) {
            return "redirect:/escolas-view?erro=true";
        }

        EscolaRequestDTO dto = new EscolaRequestDTO();
        dto.setId(escola.getId());
        dto.setNome(escola.getNome());
        dto.setCodigoInep(escola.getCodigoInep());
        dto.setCnpj(escola.getCnpj());
        dto.setEndereco(escola.getEndereco());
        dto.setTelefone(escola.getTelefone());
        dto.setEmail(escola.getEmail());
        dto.setStatus(escola.getStatus());

        model.addAttribute("escola", dto);
        model.addAttribute("modoEdicao", true);
        return "escola-form";
    }

    @PostMapping("/escolas/salvar")
    public String salvarEscola(@ModelAttribute("escola") EscolaRequestDTO dto, Model model) {

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            model.addAttribute("erro", "O nome da escola é obrigatório.");
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "escola-form";
        }

        try {
            Escola escola;

            if (dto.getId() != null) {
                escola = escolaRepository.findById(dto.getId()).orElse(null);
                if (escola == null) {
                    return "redirect:/escolas-view?erro=true";
                }
            } else {
                escola = new Escola();
            }

            escola.setNome(dto.getNome());
            escola.setCodigoInep(dto.getCodigoInep());
            escola.setCnpj(dto.getCnpj());
            escola.setEndereco(dto.getEndereco());
            escola.setTelefone(dto.getTelefone());
            escola.setEmail(dto.getEmail());
            escola.setStatus(dto.getStatus() != null && !dto.getStatus().isBlank() ? dto.getStatus() : "ATIVA");

            escolaRepository.save(escola);

            if (dto.getId() != null) {
                return "redirect:/escolas-view?editado=true";
            }

            return "redirect:/escolas-view?sucesso=true";

        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível salvar a escola.");
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "escola-form";
        }
    }

    @GetMapping("/escolas/excluir/{id}")
    public String excluirEscola(@PathVariable Long id) {
        try {
            escolaRepository.deleteById(id);
            return "redirect:/escolas-view?excluido=true";
        } catch (Exception e) {
            return "redirect:/escolas-view?erro=true";
        }
    }
}