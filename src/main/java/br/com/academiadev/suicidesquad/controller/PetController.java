package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.dto.PetCreateDTO;
import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.dto.PetDetailDTO;
import br.com.academiadev.suicidesquad.dto.RegistroCreateDTO;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetSearch;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.PetNotFoundException;
import br.com.academiadev.suicidesquad.mapper.PetMapper;
import br.com.academiadev.suicidesquad.mapper.RegistroMapper;
import br.com.academiadev.suicidesquad.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class PetController {

    private final PetService petService;

    private final PetMapper petMapper;

    private final RegistroMapper registroMapper;

    @Autowired
    public PetController(PetService petService, PetMapper petMapper, RegistroMapper registroMapper) {
        this.petService = petService;
        this.petMapper = petMapper;
        this.registroMapper = registroMapper;
    }

    @GetMapping("/pets/search")
    public Iterable<PetDTO> getPets(@Valid PetSearch search) {
        return petMapper.toDtos(petService.search(search));
    }

    @PostMapping("/pets")
    @ResponseStatus(HttpStatus.CREATED)
    PetDetailDTO createPet(@Valid @RequestBody PetCreateDTO petCreateDTO, @AuthenticationPrincipal Usuario usuarioLogado) {
        final Pet pet = petMapper.toEntity(petCreateDTO);
        pet.setUsuario(usuarioLogado);
        return petMapper.toDetailDto(petService.save(pet));
    }

    @GetMapping("/pets/{idPet}")
    public PetDetailDTO getPet(@PathVariable Long idPet) {
        return petService
                .findById(idPet)
                .map(petMapper::toDetailDto)
                .orElseThrow(PetNotFoundException::new);
    }

    @DeleteMapping("/pets/{idPet}")
    @PreAuthorize("@petService.isPublicador(authentication, #idPet)")
    public ResponseEntity deletePet(@PathVariable Long idPet) {
        final Pet pet = petService.findById(idPet).orElse(null);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        petService.deleteById(idPet);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/pets/{idPet}")
    @PreAuthorize("@petService.isPublicador(authentication, #idPet)")
    public ResponseEntity editPet(@PathVariable Long idPet, @Valid @RequestBody PetCreateDTO petCreateDTO) {
        Pet pet = petService.findById(idPet).orElse(null);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        petMapper.updateEntity(petCreateDTO, pet);
        petService.save(pet);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pets/{idPet}/registros")
    @PreAuthorize("@petService.isPublicador(authentication, #idPet)")
    public ResponseEntity addRegistro(@PathVariable Long idPet, @Valid @RequestBody RegistroCreateDTO registroCreateDTO) {
        Pet pet = petService.findById(idPet).orElse(null);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        pet.addRegistro(registroMapper.toEntity(registroCreateDTO));
        return ResponseEntity.ok().build();
    }
}
