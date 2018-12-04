package br.com.academiadev.suicidesquad.controller;


import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.dto.PetFavoritoDTO;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetFavorito;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.PetNotFoundException;
import br.com.academiadev.suicidesquad.mapper.PetFavoritoMapper;
import br.com.academiadev.suicidesquad.mapper.PetMapper;
import br.com.academiadev.suicidesquad.service.PetFavoritoService;
import br.com.academiadev.suicidesquad.service.PetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class PetFavoritoController {
    private final PetMapper petMapper;

    private final PetService petService;

    private final PetFavoritoService petFavoritoService;

    private final PetFavoritoMapper petFavoritoMapper;

    @Autowired
    public PetFavoritoController(PetMapper petMapper, PetService petService, PetFavoritoService petFavoritoService, PetFavoritoMapper petFavoritoMapper) {
        this.petMapper = petMapper;
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
    PetFavorito createFavorito(@PathVariable Long idPet, @Valid @AuthenticationPrincipal Usuario usuarioLogado) {
        Pet pet = petService.findById(idPet).get();
        PetFavorito petFavorito = PetFavorito.builder()
                .pet(pet)
                .usuario(usuarioLogado).build();
        pet.addPetFavorito(petFavorito);
        usuarioLogado.addPetFavorito(petFavorito);

        return petFavoritoService.save(petFavorito);
    }

    @DeleteMapping("/favoritos/{idPet}")
    public void deletarFavorito(@PathVariable Long idPet, @Valid @AuthenticationPrincipal Usuario usuarioLogado) {
        PetFavorito petFavorito = petFavoritoService.findByIdPetAndIdUsuario(idPet, usuarioLogado.getId()).get();

        Pet pet = petService.findById(idPet).get();

        pet.getPetFavoritos().remove(petFavorito);
        usuarioLogado.getPetFavoritos().remove(petFavorito);

        petFavoritoService.deleteById(petFavorito.getId());
    }


}
