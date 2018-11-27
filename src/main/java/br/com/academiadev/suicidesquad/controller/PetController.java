package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.dto.PetCreateDTO;
import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.dto.PetDetailDTO;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetSearch;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.ResourceNotFoundException;
import br.com.academiadev.suicidesquad.mapper.PetMapper;
import br.com.academiadev.suicidesquad.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class PetController {

    private final PetService petService;

    private final PetMapper petMapper;

    @Autowired
    public PetController(PetService petService, PetMapper petMapper) {
        this.petService = petService;
        this.petMapper = petMapper;
    }

    @GetMapping("/pets/search")
    public Iterable<PetDTO> getPets(@Valid PetSearch search) {
        return petMapper.toDtos(petService.search(search));
    }

    @PostMapping("/pets")
    @ResponseStatus(HttpStatus.CREATED)
    PetDetailDTO createPet(@Valid @RequestBody PetCreateDTO petCreateDTO) {
        return petMapper.toDetailDto(petService.save(petMapper.toEntity(petCreateDTO)));
    }

    @GetMapping("/pets/{idPet}")
    public PetDetailDTO getPet(@PathVariable Long idPet) {
        return petService
                .findById(idPet)
                .map(petMapper::toDetailDto)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com o id " + idPet + " não foi encontrado"));
    }

    @DeleteMapping("/pets/{idPet}")
    public ResponseEntity deletePet(@PathVariable Long idPet, @AuthenticationPrincipal Usuario usuarioLogado) {
        final Pet pet = petService.findById(idPet).orElse(null);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        if (!pet.getUsuario().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        petService.deleteById(idPet);
        return ResponseEntity.ok().build();
    }
}
