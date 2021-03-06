package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioCreateDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioEditDTO;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.EmailExistenteException;
import br.com.academiadev.suicidesquad.exception.UsuarioNotFoundException;
import br.com.academiadev.suicidesquad.mapper.PetMapper;
import br.com.academiadev.suicidesquad.mapper.UsuarioMapper;
import br.com.academiadev.suicidesquad.service.PetService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UsuarioController {

    /*
    Regra de negócio não deve estar no controller, apenas no service
    */

    private final UsuarioService usuarioService;

    private final PetService petService;

    private final UsuarioMapper usuarioMapper;

    private final PetMapper petMapper;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper, PetService petService, PetMapper petMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
        this.petService = petService;
        this.petMapper = petMapper;
    }

    @ApiOperation(value = "Retorna o usuário pelo Id")
    @GetMapping("/usuarios/{idUsuario}")
    public UsuarioDTO getUsuarioById(@PathVariable Long idUsuario) {
        return usuarioService.findById(idUsuario)
                .map(usuarioMapper::toDto)
                .orElseThrow(UsuarioNotFoundException::new);
    }

    @ApiOperation(value = "Cria o usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuário criado com sucesso"),
            @ApiResponse(code = 400, message = "Usuário inválido"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/usuarios")
    UsuarioDTO createUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        if (usuarioService.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new EmailExistenteException();
        }
        Usuario usuario = usuarioMapper.toEntity(usuarioCreateDTO);
        return usuarioMapper.toDto(usuarioService.save(usuario));
    }

    @DeleteMapping("/usuarios/{idUsuario}")
    public ResponseEntity deleteUsuario(@PathVariable Long idUsuario, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (!usuarioLogado.getId().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        usuarioService.deleteById(idUsuario);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/usuarios/{idUsuario}")
    public ResponseEntity editUsuario(@PathVariable Long idUsuario, @Valid @RequestBody UsuarioEditDTO usuarioEditDTO, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (!usuarioLogado.getId().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.notFound().build();
        }
        usuarioMapper.updateEntity(usuarioEditDTO, usuarioLogado);
        usuarioService.save(usuarioLogado);
        return ResponseEntity.ok().build();
    }

    @ApiResponses({
            @ApiResponse(code=200, message = "List de Pets do Usuário encontrada")
    })
    @GetMapping("/usuarios/{idUsuario}/pets")
    public Iterable<PetDTO> getPetsByIdUsuario(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioService.findById(idUsuario)
                .orElseThrow(UsuarioNotFoundException::new);
        List<Pet> pets = petService.findPetsByUsuario(usuario);
        return petMapper.toDtos(pets);
    }
}
