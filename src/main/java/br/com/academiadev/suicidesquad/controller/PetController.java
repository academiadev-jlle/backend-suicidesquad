package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.dto.PetCreateDTO;
import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.dto.PetDetailDTO;
import br.com.academiadev.suicidesquad.dto.RegistroCreateDTO;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetSearch;
import br.com.academiadev.suicidesquad.entity.Registro;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.ResourceNotFoundException;
import br.com.academiadev.suicidesquad.mapper.PetMapper;
import br.com.academiadev.suicidesquad.mapper.RegistroMapper;
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

    @PutMapping("/pets/{idPet}")
    public ResponseEntity editPet(@PathVariable Long idPet, @Valid @RequestBody PetCreateDTO petCreateDTO, @AuthenticationPrincipal Usuario usuarioLogado) {
        Pet pet = petService.findById(idPet).orElse(null);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        if (!pet.getUsuario().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        petMapper.updateEntity(petCreateDTO, pet);
        petService.save(pet);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pets/{idPet}/registros")
    public ResponseEntity addRegistro(@PathVariable Long idPet, @Valid @RequestBody RegistroCreateDTO registroCreateDTO, @AuthenticationPrincipal Usuario usuarioLogado) {
        Pet pet = petService.findById(idPet).orElse(null);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        if (!pet.getUsuario().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        pet.addRegistro(registroMapper.toEntity(registroCreateDTO));
        return ResponseEntity.ok().build();
    }
}
