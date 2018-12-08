package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetSearch;
import br.com.academiadev.suicidesquad.enums.*;
import br.com.academiadev.suicidesquad.repository.PetRepository;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PetServiceSearchTest {
    @Autowired
    private PetService petService;

    private Pet petGatoA = Pet.builder()
            .tipo(Tipo.GATO)
            .porte(Porte.MEDIO)
            .comprimentoPelo(ComprimentoPelo.MEDIO)
            .categoria(Categoria.ACHADO)
            .sexo(SexoPet.FEMEA)
            .build();
    private Pet petCachorroA = Pet.builder()
            .tipo(Tipo.CACHORRO)
            .porte(Porte.PEQUENO)
            .comprimentoPelo(ComprimentoPelo.MEDIO)
            .categoria(Categoria.ACHADO)
            .castracao(Castracao.CASTRADO)
            .vacinacao(Vacinacao.EM_DIA)
            .sexo(SexoPet.MACHO)
            .build();
    private Pet petCachorroB = Pet.builder()
            .tipo(Tipo.CACHORRO)
            .porte(Porte.GRANDE)
            .comprimentoPelo(ComprimentoPelo.LONGO)
            .categoria(Categoria.PERDIDO)
            .vacinacao(Vacinacao.PARCIAL)
            .build();

    @Before
    public void registrarPets() {
        petService.save(petGatoA);
        petService.save(petCachorroA);
        petService.save(petCachorroB);
    }

    @Test
    public void pesquisarPorTipo() {
        final Iterable<Pet> result = petService.search(PetSearch.builder().tipo(Tipo.CACHORRO).build());
        assertThat(result, contains(petCachorroA, petCachorroB));
    }

    @Test
    public void pesquisarExato() {
        final Iterable<Pet> result = petService.search(PetSearch.builder()
                .tipo(Tipo.CACHORRO)
                .portes(Collections.singleton(Porte.PEQUENO))
                .pelos(Collections.singleton(ComprimentoPelo.MEDIO))
                .categorias(Collections.singleton(Categoria.ACHADO))
                .castracoes(Collections.singleton(Castracao.CASTRADO))
                .vacinacoes(Collections.singleton(Vacinacao.EM_DIA))
                .sexo(SexoPet.MACHO)
                .build());
        assertThat(result, contains(petCachorroA));
    }
}
