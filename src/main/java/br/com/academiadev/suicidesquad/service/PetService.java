package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Optional<Pet> findById(Long idPet) {
        return petRepository.findById(idPet);
    }

    public Page<Pet> findAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }
}
