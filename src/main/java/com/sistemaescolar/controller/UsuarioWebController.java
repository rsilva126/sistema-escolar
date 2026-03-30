package com.sistemaescolar.controller;

import com.sistemaescolar.dto.UsuarioRequestDTO;
import com.sistemaescolar.dto.UsuarioResponseDTO;
import com.sistemaescolar.model.Escola;
import com.sistemaescolar.model.Usuario;
import com.sistemaescolar.repository.EscolaRepository;
import com.sistemaescolar.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class UsuarioWebController {

    private final UsuarioRepository usuarioRepository;
    private final EscolaRepository escolaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioWebController(UsuarioRepository usuarioRepository,
                                EscolaRepository escolaRepository,
                                PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.escolaRepository = escolaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/usuarios-view")
    public String listarUsuariosPagina(
            Model model,
            @RequestParam(value = "sucesso", required = false) String sucesso,
            @RequestParam(value = "excluido", required = false) String excluido,
            @RequestParam(value = "editado", required = false) String editado,
            @RequestParam(value = "erro", required = false) String erro,
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "escolaId", required = false) Long escolaId
    ) {
        List<Usuario> usuarios;

        boolean temBusca = busca != null && !busca.isBlank();
        boolean temEscola = escolaId != null;

        if (temBusca && temEscola) {
            usuarios = usuarioRepository.findByNomeContainingIgnoreCaseAndEscola_Id(busca, escolaId);
        } else if (temBusca) {
            usuarios = usuarioRepository.findByNomeContainingIgnoreCase(busca);
        } else if (temEscola) {
            usuarios = usuarioRepository.findByEscola_Id(escolaId);
        } else {
            usuarios = usuarioRepository.findAll();
        }

        List<UsuarioResponseDTO> lista = usuarios.stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getCpf(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        usuario.getEscola() != null ? usuario.getEscola().getNome() : "",
                        usuario.getStatus(),
                        usuario.getFuncao()
                ))
                .collect(Collectors.toList());

        model.addAttribute("usuarios", lista);
        model.addAttribute("busca", busca);
        model.addAttribute("escolaId", escolaId);
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("totalItens", lista.size());

        if (escolaId != null) {
            escolaRepository.findById(escolaId)
                    .ifPresent(escola -> model.addAttribute("nomeEscolaFiltro", escola.getNome()));
        }

        if (sucesso != null) {
            model.addAttribute("mensagemSucesso", "Usuário cadastrado com sucesso.");
        }

        if (excluido != null) {
            model.addAttribute("mensagemSucesso", "Usuário excluído com sucesso.");
        }

        if (editado != null) {
            model.addAttribute("mensagemSucesso", "Usuário atualizado com sucesso.");
        }

        if (erro != null) {
            model.addAttribute("mensagemErro", "Não foi possível concluir a operação.");
        }

        return "usuarios";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDTO());
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("modoEdicao", false);
        return "usuario-form";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            return "redirect:/usuarios-view?erro=true";
        }

        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setCpf(usuario.getCpf());
        dto.setEmail(usuario.getEmail());
        dto.setSenha(usuario.getSenha());
        dto.setTelefone(usuario.getTelefone());
        dto.setEscolaId(usuario.getEscola() != null ? usuario.getEscola().getId() : null);
        dto.setStatus(usuario.getStatus());
        dto.setFuncao(usuario.getFuncao());

        model.addAttribute("usuario", dto);
        model.addAttribute("escolas", escolaRepository.findAll());
        model.addAttribute("modoEdicao", true);

        return "usuario-form";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute("usuario") UsuarioRequestDTO dto, Model model) {

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            model.addAttribute("erro", "O nome do usuário é obrigatório.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "usuario-form";
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            model.addAttribute("erro", "O email é obrigatório.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "usuario-form";
        }

        if (dto.getEscolaId() == null) {
            model.addAttribute("erro", "A escola é obrigatória.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "usuario-form";
        }

        Escola escola = escolaRepository.findById(dto.getEscolaId()).orElse(null);

        if (escola == null) {
            model.addAttribute("erro", "Escola não encontrada.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "usuario-form";
        }

        try {
            Usuario usuario;

            if (dto.getId() != null) {
                usuario = usuarioRepository.findById(dto.getId()).orElse(null);
                if (usuario == null) {
                    return "redirect:/usuarios-view?erro=true";
                }
            } else {
                usuario = new Usuario();
            }

            usuario.setNome(dto.getNome());
            usuario.setCpf(dto.getCpf());
            usuario.setEmail(dto.getEmail());

            if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
                usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
            } else if (dto.getId() == null) {
                model.addAttribute("erro", "A senha é obrigatória.");
                model.addAttribute("escolas", escolaRepository.findAll());
                model.addAttribute("modoEdicao", false);
                return "usuario-form";
            }

            usuario.setTelefone(dto.getTelefone());
            usuario.setEscola(escola);
            usuario.setStatus(dto.getStatus() != null && !dto.getStatus().isBlank() ? dto.getStatus() : "ATIVO");
            usuario.setFuncao(dto.getFuncao() != null && !dto.getFuncao().isBlank() ? dto.getFuncao() : "PROFESSOR");

            usuarioRepository.save(usuario);

            if (dto.getId() != null) {
                return "redirect:/usuarios-view?editado=true";
            }

            return "redirect:/usuarios-view?sucesso=true";

        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível salvar o usuário. Verifique email e CPF.");
            model.addAttribute("escolas", escolaRepository.findAll());
            model.addAttribute("modoEdicao", dto.getId() != null);
            return "usuario-form";
        }
    }

    @GetMapping("/usuarios/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        try {
            usuarioRepository.deleteById(id);
            return "redirect:/usuarios-view?excluido=true";
        } catch (Exception e) {
            return "redirect:/usuarios-view?erro=true";
        }
    }
}