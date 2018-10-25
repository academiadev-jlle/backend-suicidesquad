package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.exception.ResourceNotFoundException;
import br.com.academiadev.suicidesquad.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PetController {
    @Autowired
    private PetRepository petRepository;

    @GetMapping("/pets")
    public Page<Pet> getPets(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    @PostMapping Pet createPet(@Valid @RequestBody Pet pet) {
        return petRepository.save(pet);
    }

    @GetMapping("/pets/{idPet}")
    public Pet getPet(@PathVariable Long idPet) {
        return petRepository
                .findById(idPet)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com o id " + idPet + " não foi encontrado"));
    }
}
