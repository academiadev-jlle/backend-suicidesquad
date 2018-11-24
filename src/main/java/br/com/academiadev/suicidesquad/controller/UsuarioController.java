package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.dto.UsuarioCreateDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioDTO;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.mapper.UsuarioMapper;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController

@CrossOrigin
public class UsuarioController {

    /*
    Regra de negócio não deve estar no controller, apenas no service
    */

    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @ApiOperation(value = "Retorna o usuário pelo Id")
    @GetMapping("/usuarios/{idUsuario}")
    public UsuarioDTO getUsuarioById(@PathVariable Long idUsuario) {
        return usuarioService.findById(idUsuario)
                .map(usuarioMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com o id" + idUsuario + " não foi encontrado"));
    }

    @ApiOperation(value = "Cria o usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuário criado com sucesso")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/usuarios")
    UsuarioDTO createUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioCreateDTO);
        return usuarioMapper.toDto(usuarioService.save(usuario));
    }
}
