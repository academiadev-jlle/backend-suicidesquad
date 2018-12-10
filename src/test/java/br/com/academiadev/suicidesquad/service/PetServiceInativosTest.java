package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.Categoria;
import br.com.academiadev.suicidesquad.enums.ComprimentoPelo;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Tipo;
import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PetServiceInativosTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    PetService petService;

    @Autowired
    UsuarioService usuarioService;

    private Usuario buildUsuarioA() {
        return Usuario.builder()
                .nome("Usuário A")
                .email("usuario.a@example.com")
                .senha("hunter2")
                .build();
    }

    private Usuario buildUsuarioB() {
        return Usuario.builder()
                .nome("Usuário B")
                .email("usuario.b@example.com")
                .senha("hunter2")
                .build();
    }

    private Pet buildPet(Usuario usuario) {
        return Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .build();
    }

    @Test
    public void dadoPetInativo_quandoBuscarPetsInativos_entaoRetornaPetInativo() {
        Usuario usuario = buildUsuarioA();
        Pet pet = buildPet(usuario);

        Pet pet2 = buildPet(usuario);

        petService.save(pet);
        petService.save(pet2);
        usuarioService.save(usuario);

        List<Pet> petsInativos = new ArrayList<>();
        petsInativos.add(pet);

        pet.getRegistros().get(0).setData(LocalDateTime.now().minusDays(9));

        List<Pet> petsInativosRecebidos = petService.findPetsInativosNaoNotificadosDoUsuario(usuario);

        assertThat(pet.getRegistros().size(), equalTo(1));
        assertThat(petsInativos, equalTo(petsInativosRecebidos));
    }

    @Test
    public void dadoPetsAtivos_quandoBuscarPetsInativos_entaoNenhumPetEncontrado() {
        Usuario usuario = buildUsuarioA();

        petService.save(buildPet(usuario));
        petService.save(buildPet(usuario));
        petService.save(buildPet(usuario));
        usuarioService.save(usuario);

        List<Pet> petsNaoEncontrados = petService.findPetsInativosNaoNotificadosDoUsuario(usuario);

        assertThat(petsNaoEncontrados, empty());
    }

    @Test
    public void dadoPetsInativos_quandoBuscarPertInativos_entaoPetsInativosRetornados() {
        Usuario usuario = buildUsuarioA();

        List<Pet> petsInativos = new ArrayList<>();

        petsInativos.add(petService.save(buildPet(usuario)));
        petsInativos.add(petService.save(buildPet(usuario)));
        petsInativos.add(petService.save(buildPet(usuario)));
        petsInativos.add(petService.save(buildPet(usuario)));

        petsInativos.forEach(pet -> pet.getRegistros().get(0).setData(LocalDateTime.now().minusWeeks(2)));

        usuarioService.save(usuario);

        List<Pet> petsRetornados = petService.findPetsInativosNaoNotificadosDoUsuario(usuario);

        assertThat(petsInativos, equalTo(petsRetornados));
    }
}
