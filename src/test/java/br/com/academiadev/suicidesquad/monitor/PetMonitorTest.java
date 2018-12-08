package br.com.academiadev.suicidesquad.monitor;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.Categoria;
import br.com.academiadev.suicidesquad.enums.ComprimentoPelo;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Tipo;
import br.com.academiadev.suicidesquad.service.EmailService;
import br.com.academiadev.suicidesquad.service.PetService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PetMonitor.class})
public class PetMonitorTest {
    @Autowired
    private PetMonitor petMonitor;

    @MockBean
    UsuarioService usuarioService;

    @MockBean
    PetService petService;

    @MockBean
    EmailService emailService;

    @Test
    public void quandoTemPetsInativos_entaoNotificaUsuarios() {
        Usuario usuario = mock(Usuario.class);
        when(usuario.getNome()).thenReturn("Fulano");
        when(usuarioService.findAll()).thenReturn(Collections.singletonList(usuario));

        List<Pet> pets = new ArrayList<>();
        pets.add(Pet.builder()
                .usuario(usuario)
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .nome("Bidu")
                .build());
        pets.add(Pet.builder()
                .usuario(usuario)
                .tipo(Tipo.EQUINO)
                .porte(Porte.GRANDE)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.PARA_ADOCAO)
                .build());
        when(petService.findPetsInativosNaoNotificadosDoUsuario(usuario)).thenReturn(pets);

        petMonitor.notificarInatividade();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).enviarParaUsuario(eq(usuario), anyString(), captor.capture());

        String captured = captor.getValue();
        assertThat(captured, containsString(usuario.getNome()));
        assertThat(captured, containsString(pets.get(0).getNome()));
    }
}