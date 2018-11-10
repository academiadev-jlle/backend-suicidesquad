package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.exception.ResourceNotFoundException;
import br.com.academiadev.suicidesquad.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/pets")
    public Page<Pet> getPets(Pageable pageable) {
        return petService.findAll(pageable);
    }

    @PostMapping("/pets")
    Pet createPet(@Valid @RequestBody Pet pet) {
        return petService.save(pet);
    }

    @GetMapping("/pets/{idPet}")
    public Pet getPet(@PathVariable Long idPet) {
        return petService
                .findById(idPet)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com o id " + idPet + " não foi encontrado"));
    }
}
