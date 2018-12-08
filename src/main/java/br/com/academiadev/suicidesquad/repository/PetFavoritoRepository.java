package br.com.academiadev.suicidesquad.repository;

import br.com.academiadev.suicidesquad.entity.PetFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetFavoritoRepository extends JpaRepository<PetFavorito, Long> {

}
