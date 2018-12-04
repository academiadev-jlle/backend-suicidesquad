package br.com.academiadev.suicidesquad.service;


import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetFavorito;
import br.com.academiadev.suicidesquad.repository.PetFavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetFavoritoService {
    private final PetFavoritoRepository petFavoritoRepository;


    @Autowired
    public PetFavoritoService(PetFavoritoRepository petFavoritoRepository) {
        this.petFavoritoRepository = petFavoritoRepository;
    }

    public List<Pet> findAllPetsByUsuarioId(Long idUsuario) {
        List<Pet> pets = new ArrayList<>();
        petFavoritoRepository.findAll().stream().filter(petFavorito -> petFavorito.getUsuario().getId() == idUsuario)
                .forEach(petFavorito -> pets.add(petFavorito.getPet()));
        return pets;
    }

    public Optional<PetFavorito> findById(Long id) {
        return petFavoritoRepository.findById(id);
    }

    public List<PetFavorito> findAll() {
        return petFavoritoRepository.findAll();
    }

    public PetFavorito save(PetFavorito petFavorito) {
        return petFavoritoRepository.save(petFavorito);
    }

    public void deleteById(Long id) {
        petFavoritoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return petFavoritoRepository.existsById(id);
    }

    public boolean existsPetFavorito(Long idPet, Long idUsuario) {
        return petFavoritoRepository.findAll().stream()
                .anyMatch(petFavorito -> (petFavorito.getUsuario().getId() == idUsuario) &&
                        petFavorito.getPet().getId() == idPet);
    }

    public Optional<PetFavorito> findByIdPetAndIdUsuario(Long idPet, Long idUsuario) {
        return petFavoritoRepository.findAll().stream().filter(petFavorito ->
                (petFavorito.getPet().getId() == idPet) && (petFavorito.getUsuario().getId() == idUsuario)).findFirst();
    }
}
