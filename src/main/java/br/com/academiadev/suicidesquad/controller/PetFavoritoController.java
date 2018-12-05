package br.com.academiadev.suicidesquad.controller;


import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetFavorito;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.mapper.PetFavoritoMapper;
import br.com.academiadev.suicidesquad.mapper.PetMapper;
import br.com.academiadev.suicidesquad.service.PetFavoritoService;
import br.com.academiadev.suicidesquad.service.PetService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class PetFavoritoController {
    private final PetMapper petMapper;

    private final UsuarioService usuarioService;

    private final PetService petService;

    private final PetFavoritoService petFavoritoService;

    private final PetFavoritoMapper petFavoritoMapper;

    @Autowired
    public PetFavoritoController(PetMapper petMapper, UsuarioService usuarioService, PetService petService, PetFavoritoService petFavoritoService, PetFavoritoMapper petFavoritoMapper) {
        this.petMapper = petMapper;
        this.usuarioService = usuarioService;
        this.petService = petService;
        this.petFavoritoService = petFavoritoService;
        this.petFavoritoMapper = petFavoritoMapper;
    }

    @ApiOperation(value = "Retorna todos os pets favoritados do usuário logado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de pets favoritados encontrada")
    })
    @GetMapping("/favoritos/")
    public Iterable<PetDTO> getAllPetsFavoritos(@Valid @AuthenticationPrincipal Usuario usuarioLogado) {
        return petMapper.toDtos(petFavoritoService.findAllPetsByUsuarioId(usuarioLogado.getId()));
    }

    @PostMapping("/favoritos/{idPet}/")
    @ResponseStatus(HttpStatus.CREATED)
    public PetFavorito createFavorito(@PathVariable Long idPet, @Valid @AuthenticationPrincipal Usuario usuarioLogado) {
        Pet pet = petService.findById(idPet).get();
        Usuario usuario = usuarioService.findById(usuarioLogado.getId()).get();

        PetFavorito petFavorito = new PetFavorito(usuario, pet);
        pet.addPetFavorito(petFavorito);
        usuario.addPetFavorito(petFavorito);

        return petFavoritoService.saveIfNotExists(petFavorito);
    }

    @DeleteMapping("/favoritos/{idPet}")
    public void deletarFavorito(@PathVariable Long idPet, @Valid @AuthenticationPrincipal Usuario usuarioLogado) {
        PetFavorito petFavorito = petFavoritoService.findByIdPetAndIdUsuario(idPet, usuarioLogado.getId()).orElse(null);

        if (petFavorito != null) {
            Usuario usuario = usuarioService.findById(usuarioLogado.getId()).get();
            Pet pet = petService.findById(idPet).get();

            pet.getPetFavoritos().remove(petFavorito);
            usuario.getPetFavoritos().remove(petFavorito);
            petFavoritoService.deleteById(petFavorito.getId());
        }
    }


}
