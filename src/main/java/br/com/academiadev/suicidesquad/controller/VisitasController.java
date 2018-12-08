package br.com.academiadev.suicidesquad.controller;


import br.com.academiadev.suicidesquad.dto.VisitaDTO;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.mapper.VisitaMapper;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/visitas")
public class VisitasController {

    private final UsuarioService usuarioService;

    private final VisitaMapper visitaMapper;

    @Autowired
    public VisitasController(UsuarioService usuarioService, VisitaMapper visitaMapper) {
        this.usuarioService = usuarioService;
        this.visitaMapper = visitaMapper;
    }

    @GetMapping
    @ApiOperation(value = "Obtem as visitas do usuário logado")
    public List<VisitaDTO> getVisitasDoUsuario(@AuthenticationPrincipal Usuario usuarioLogado) {
        Usuario usuario = usuarioService.findById(usuarioLogado.getId())
                .orElseThrow(() -> {
                    return new RuntimeException("Usuário deixou de existir no meio do request");
                });
        return visitaMapper.toDtos(usuario.getVisitas());
    }
}
