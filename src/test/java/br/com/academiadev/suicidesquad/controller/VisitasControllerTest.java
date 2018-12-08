package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.Categoria;
import br.com.academiadev.suicidesquad.enums.ComprimentoPelo;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Tipo;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
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

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VisitasControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PetService petService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Usuario buildUsuario() {
        return Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .senha("hunter2")
                .build();
    }

    @Test
    public void obterVisitasDoUsuario_quandoExistem_entaoRetorna() throws Exception {
        final Usuario usuario = usuarioService.save(buildUsuario());
        final Pet petA = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .build());
        final Pet petB = petService.save(Pet.builder()
                .tipo(Tipo.CACHORRO)
                .porte(Porte.PEQUENO)
                .comprimentoPelo(ComprimentoPelo.CURTO)
                .categoria(Categoria.ACHADO)
                .build());
        petService.adicionarVisita(petA, usuario);
        petService.adicionarVisita(petB, usuario);

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(get("/visitas")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pet.tipo", equalTo(petA.getTipo().toString())))
                .andExpect(jsonPath("$[1].pet.tipo", equalTo(petB.getTipo().toString())));
    }
}