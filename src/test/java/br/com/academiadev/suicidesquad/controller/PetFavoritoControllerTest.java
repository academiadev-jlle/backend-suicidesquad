package br.com.academiadev.suicidesquad.controller;


import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.PetFavorito;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.*;
import br.com.academiadev.suicidesquad.exception.PetFavoritoNotFoundException;
import br.com.academiadev.suicidesquad.exception.PetNotFoundException;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import br.com.academiadev.suicidesquad.service.PetFavoritoService;
import br.com.academiadev.suicidesquad.service.PetService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PetFavoritoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PetService petService;

    @Autowired
    private PetFavoritoService petFavoritoService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


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
    public void dadoPetFavorito_quandoBuscarPorId_entaoEncontrado() throws Exception {
        final Usuario usuario = buildUsuario();
        usuarioService.save(usuario);

        final Pet petA = buildPetA(usuario);
        final Pet petB = buildPetB(usuario);

        petService.save(petA);
        petService.save(petB);

        final PetFavorito petFavorito = PetFavorito.builder()
                .pet(petA)
                .usuario(usuario)
                .build();

        petFavoritoService.save(petFavorito);

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(get(String.format("/favoritos/%d/", petFavorito.getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertThat(petFavorito.getPet(), equalTo(petA));
        assertThat(petFavorito.getUsuario(), equalTo(usuario));
    }

    @Test
    public void dadoUsuario_petFavoritado_entaoPetAdicionado() throws Exception {
        Usuario usuario = buildUsuario();
        usuarioService.save(usuario);

        Pet pet = buildPetA(usuario);
        petService.save(pet);

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(post(String.format("/favoritos/%d/", pet.getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertThat(usuario.getPetFavoritos().size(), equalTo(1));
        assertThat(usuario.getPetFavoritos().get(0).getPet(), equalTo(pet));
    }

    @Test
    public void dadoUsuario_petFavorito_entaoPetDesfavoritado() throws Exception {
        Usuario usuario = buildUsuario();
        usuarioService.save(usuario);

        Pet pet = buildPetA(usuario);
        petService.save(pet);

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(post(String.format("/favoritos/%d/", pet.getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mvc.perform(post(String.format("/favoritos/%d/", pet.getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertThat(usuario.getPetFavoritos().size(), equalTo(0));
    }

    @Test
    public void dadoPetFavorito_quandoExistsById_entaoVerdadeiro() {
        final Usuario usuario = buildUsuario();
        usuarioService.save(usuario);

        final Pet petA = buildPetA(usuario);
        final Pet petB = buildPetB(usuario);

        petService.save(petA);
        petService.save(petB);

        final PetFavorito petFavorito = PetFavorito.builder()
                .pet(petA)
                .usuario(usuario)
                .build();
        petFavoritoService.save(petFavorito);

        assertThat(petFavoritoService.existsPetFavorito(petA.getId(), usuario.getId()), equalTo(true));
    }

    @Test
    public void dadoPetFavoritoNaoExistente_quandoExistsById_entaoFalso() {
        final Usuario usuario = buildUsuario();
        usuarioService.save(usuario);

        final Pet petA = buildPetA(usuario);
        final Pet petB = buildPetB(usuario);

        petService.save(petA);
        petService.save(petB);

        final PetFavorito petFavorito = PetFavorito.builder()
                .pet(petA)
                .usuario(usuario)
                .build();
        petFavoritoService.save(petFavorito);

        assertThat(petFavoritoService.existsPetFavorito(petB.getId(), usuario.getId()), equalTo(false));
        assertThat(petFavoritoService.existsPetFavorito(petA.getId(), 2l), equalTo(false));
    }

    @Test
    public void dadoPetFavoritoExistente_quandoBuscarPorIdUsuarioEIdPet_entaoEncontrado() {
        final Usuario usuario = buildUsuario();
        usuarioService.save(usuario);

        final Pet petA = buildPetA(usuario);
        final Pet petB = buildPetB(usuario);

        petService.save(petA);
        petService.save(petB);

        final PetFavorito petFavorito = PetFavorito.builder()
                .pet(petA)
                .usuario(usuario)
                .build();
        petFavoritoService.save(petFavorito);

        final PetFavorito petFavoritoB = PetFavorito.builder()
                .pet(petB)
                .usuario(usuario)
                .build();
        petFavoritoService.save(petFavoritoB);

        Optional<PetFavorito> petFavoritoRespo = petFavoritoService.findByIdPetAndIdUsuario(petA.getId(), usuario.getId());

        assertThat(petFavoritoRespo.get(), equalTo(petFavorito));
        assertThat(petFavoritoRespo.get().getUsuario(), equalTo(usuario));
        assertThat(petFavoritoRespo.get().getPet(), equalTo(petA));
    }


    @Test
    public void dadoPetFavoritoNaoExiste_quandoBuscarPorIdUsuarioEIdPet_entaoNaoEncontrado() {
        final Usuario usuario = buildUsuario();
        usuarioService.save(usuario);

        final Pet petA = buildPetA(usuario);
        final Pet petB = buildPetB(usuario);

        petService.save(petA);
        petService.save(petB);

        final PetFavorito petFavorito = PetFavorito.builder()
                .pet(petA)
                .usuario(usuario)
                .build();
        petFavoritoService.save(petFavorito);

        try {
            PetFavorito petFavoritoRespo = petFavoritoService.findByIdPetAndIdUsuario(petB.getId(),
                    usuario.getId()).orElseThrow(PetFavoritoNotFoundException::new);

        } catch (Exception e) {
            assertThat(e.getMessage(), equalTo("Pet Favorito não encontrado"));
        }

    }

    @Test
    public void dadoUsuarioComPetsFavoritos_quandoBuscaTodos_entaoForamEncontrados() throws Exception {
        Usuario usuario = buildUsuario();
        usuarioService.save(usuario);

        Pet petA = buildPetA(usuario);
        petService.save(petA);

        Pet petB = buildPetB(usuario);
        petService.save(petB);

        PetFavorito petFavoritoA = PetFavorito.builder()
                .pet(petA)
                .usuario(usuario)
                .build();

        PetFavorito petFavoritoB = PetFavorito.builder()
                .pet(petB)
                .usuario(usuario)
                .build();

        petFavoritoService.save(petFavoritoA);
        petFavoritoService.save(petFavoritoB);

        usuario.addPetFavorito(petFavoritoA);
        usuario.addPetFavorito(petFavoritoB);

        petA.addPetFavorito(petFavoritoA);
        petB.addPetFavorito(petFavoritoB);

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(get("/favoritos/")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tipo", equalTo(petA.getTipo().toString())))
                .andExpect(jsonPath("$[1].tipo", equalTo(petB.getTipo().toString())))
                .andExpect(jsonPath("$[0].porte", equalTo(petA.getPorte().toString())))
                .andExpect(jsonPath("$[1].porte", equalTo(petB.getPorte().toString())));
    }
}
