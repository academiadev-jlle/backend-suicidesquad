package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Localizacao;
import br.com.academiadev.suicidesquad.entity.Telefone;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioService usuarioService;


    private Usuario buildUsuario() {
        return Usuario.builder()
                .email("nelson@example.com")
                .nome("Nelson Nunes Guimarães")
                .senha("senha1")
                .telefone(new Telefone("47988271821"))
                .localizacao(new Localizacao())
                .dataNascimento(LocalDate.of(2000, 5, 22))
                .build();
    }

    private List<Usuario> buildUsuarios() {
        Usuario usuario1 = Usuario.builder()
                .email("nelson@example.com")
                .nome("Nelson Nunes Guimarães")
                .senha("senha1")
                .dataNascimento(LocalDate.of(1965, 5, 16))
                .build();

        Usuario usuario2 = Usuario.builder()
                .email("salvador@example.com")
                .nome("Salvador Di Bernardi")
                .senha("senha2")
                .telefone(new Telefone("47988271821"))
                .localizacao(new Localizacao())
                .dataNascimento(LocalDate.of(1983, 11, 28))
                .build();

        return Arrays.asList(usuario1,
                usuario2);
    }

    @Test
    public void dadoBuscarUsuario_quandoUsuarioExiste_entaoUsuarioEncontrado() throws Exception {
        Usuario usuario = buildUsuario();

        when(usuarioService.findById(1L)).thenReturn(java.util.Optional.of(usuario));

        this.mvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", equalTo(usuario.getNome())));


    }

    @Test
    public void dadoBuscarUsuario_quandoUsuarioNaoExiste_entaoUsuarioNaoEncontrado() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.empty());

        this.mvc.perform(get("/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void dadoCriarUsuario_quandoUsuarioInexistente_entaoUsuarioCriado() throws Exception {

        JSONObject usuarioJson = new JSONObject();
        usuarioJson.put("nome", "Nelson Nunes Guimarães");
        usuarioJson.put("email", "nelson@example.com");
        usuarioJson.put("senha", "senha1");
        usuarioJson.put("sexo", "MASCULINO");
        usuarioJson.put("data_nascimento", LocalDate.of(1990, 10, 8));

        JSONArray telefonesJson = new JSONArray();
        JSONObject telefoneJson = new JSONObject();
        telefoneJson.put("numero", "9999999");
        telefoneJson.put("whatsapp", false);
        telefonesJson.put(telefoneJson);

        usuarioJson.put("telefones", telefonesJson);


        this.mvc.perform(post("/usuarios")
                .content(usuarioJson.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        ArgumentCaptor<Usuario> argumentCaptor = ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioService, times(1)).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getNome(), equalTo(usuarioJson.getString("nome")));
    }

    @Test
    public void dadoCriarUsuario_quandoUsuarioInexistente_entaoUsuarioNaoCriado() throws Exception {
        this.mvc.perform(post("/usuarios")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).save(any(Usuario.class));

    }

}
