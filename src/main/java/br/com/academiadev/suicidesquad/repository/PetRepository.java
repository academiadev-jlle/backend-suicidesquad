package br.com.academiadev.suicidesquad.repository;

import br.com.academiadev.suicidesquad.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
