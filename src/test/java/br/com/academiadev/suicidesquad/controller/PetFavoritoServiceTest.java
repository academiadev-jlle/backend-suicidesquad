package br.com.academiadev.suicidesquad.controller;


import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetFavorito;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.*;
import br.com.academiadev.suicidesquad.exception.PetFavoritoNotFoundException;
import br.com.academiadev.suicidesquad.service.PetFavoritoService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional

public class PetFavoritoServiceTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private PetFavoritoService petFavoritoService;

    private Usuario buildUsuario() {
        return Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .senha("hunter2")
                .build();
    }

    private Pet buildPetA(Usuario usuario) {
        Set<Cor> cores = new HashSet<>();
        cores.add(Cor.PRETO);
        cores.add(Cor.BRANCO);
        return Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .cores(cores)
                .usuario(usuario)
                .build();
    }

    private Pet buildPetB(Usuario usuario) {
        Set<Cor> cores = new HashSet<>();
        cores.add(Cor.PRETO);
        cores.add(Cor.MARROM);
        return Pet.builder()
                .tipo(Tipo.EQUINO)
                .porte(Porte.GRANDE)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.PARA_ADOCAO)
                .cores(cores)
                .usuario(usuario)
                .build();
    }

    @Test
    public void dadoPetFavoritoExistente_quandoBuscarPorIdUsuarioEIdPet_entaoEncontrado() {
        final Usuario usuario = buildUsuario();
        final Pet petA = buildPetA(usuario);
        final PetFavorito petFavorito = new PetFavorito(usuario, petA);

        when(petFavoritoService.findByIdPetAndIdUsuario(petA.getId(), usuario.getId())).thenReturn(Optional.of(petFavorito));
        assertThat(petFavorito, equalTo(petFavoritoService.findByIdPetAndIdUsuario(petA.getId(), usuario.getId()).get()));
    }

    @Test
    public void dadoPetFavoritoNaoExiste_quandoBuscarPorIdUsuarioEIdPet_entaoNaoEncontrado() {
        final Usuario usuario = buildUsuario();
        final Pet petA = buildPetA(usuario);
        final Pet petB = buildPetB(usuario);

        petFavoritoService.save(new PetFavorito(usuario, petA));

        try {
            when(petFavoritoService.findByIdPetAndIdUsuario(petB.getId(),
                    usuario.getId())).thenThrow(new PetFavoritoNotFoundException());
        } catch (Exception e) {
            assertThat(e.getMessage(), equalTo("Pet Favorito não encontrado"));
        }
    }
}
