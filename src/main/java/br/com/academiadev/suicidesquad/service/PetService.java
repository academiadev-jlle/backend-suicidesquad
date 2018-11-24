package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetSearch;
import br.com.academiadev.suicidesquad.entity.QPet;
import br.com.academiadev.suicidesquad.repository.PetRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Optional<Pet> findById(Long idPet) {
        return petRepository.findById(idPet);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public Iterable<Pet> search(PetSearch search) {
        QPet pet = QPet.pet;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(pet.tipo.eq(search.getTipo()));

        if (!search.getSexos().isEmpty()) {
            builder.and(pet.sexo.in(search.getSexos()));
        }

        if (!search.getPortes().isEmpty()) {
            builder.and(pet.porte.in(search.getPortes()));
        }

        if (!search.getRacas().isEmpty()) {
            builder.and(pet.raca.in(search.getRacas()));
        }

        if (!search.getPelos().isEmpty()) {
            builder.and(pet.comprimentoPelo.in(search.getPelos()));
        }

        if (!search.getCategorias().isEmpty()) {
            builder.and(pet.categoria.in(search.getCategorias()));
        }

        if (!search.getVacinacoes().isEmpty()) {
            builder.and(pet.vacinacao.in(search.getVacinacoes()));
        }

        if (!search.getCastracoes().isEmpty()) {
            builder.and(pet.castracao.in(search.getCastracoes()));
        }

        if (!search.getCores().isEmpty()) {
            search.getCores().forEach(cor -> {
                builder.and(pet.cores.contains(cor));
            });
        }

        return petRepository.findAll(builder);
    }
}
