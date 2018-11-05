package br.com.academiadev.suicidesquad.service;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.repository.PetRepository;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }
    
    @Autowired
    private EntityManager em;

    public Optional<Pet> findById(Long idPet) {
        return petRepository.findById(idPet);
    }

    public Page<Pet> findAll(String customRequest, Pageable pageable) {
    	System.out.println(customRequest);
    	return (new PageImpl<Pet>(em.createQuery(customRequest, Pet.class).getResultList()));
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }
}
