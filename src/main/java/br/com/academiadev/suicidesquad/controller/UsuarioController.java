package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController

public class UsuarioController {

    /*
    Regra de negócio não deve estar no controller, apenas no service
    */

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ApiOperation(value = "Retorna a lista de usuários cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuários retornados com sucesso")
    })
    @GetMapping("/usuarios")
    public Page<Usuario> getUsuarios(Pageable pageable) {
        return usuarioService.findAll(pageable);
    }

    @ApiOperation(value = "Retorna o usuário pelo Id")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuário encontrado com sucesso")
    })
    @GetMapping("/usuarios/{idUsuario}")
    public Usuario getUsuarioById(@PathVariable Long idUsuario) {
        return usuarioService.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com o id" + idUsuario + " não foi encontrado"));
    }

    @ApiOperation(value = "Cria o usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuário criado com sucesso")
    })
    @PostMapping("/usuarios")
    Usuario createUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

}
