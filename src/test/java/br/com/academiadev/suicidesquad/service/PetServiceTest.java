package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.Categoria;
import br.com.academiadev.suicidesquad.enums.ComprimentoPelo;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Tipo;
import br.com.academiadev.suicidesquad.repository.PetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PetService.class})
public class PetServiceTest {

    @Autowired
    PetService petService;

    @MockBean
    PetRepository petRepository;

    @MockBean
    EntityManager entityManager;

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

    private Pet buildPet() {
        return Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .build();
    }

    @Test
    public void adicionarVisita() {
        Usuario usuarioA = buildUsuarioA();
        Usuario usuarioB = buildUsuarioB();
        Pet pet = buildPet();

        petService.adicionarVisita(pet, usuarioA);
        assertThat(pet.getNumeroDeVisitas(), equalTo(1));
        petService.adicionarVisita(pet, usuarioB);
        assertThat(pet.getNumeroDeVisitas(), equalTo(2));
    }
}