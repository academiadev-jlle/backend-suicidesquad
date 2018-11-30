package br.com.academiadev.suicidesquad.repository;

import br.com.academiadev.suicidesquad.entity.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
    Optional<Localizacao> findByBairroAndCidadeAndUf(String bairro, String cidade, String uf);
}
