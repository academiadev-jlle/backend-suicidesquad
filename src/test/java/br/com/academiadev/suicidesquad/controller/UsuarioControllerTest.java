package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    ObjectMapper objectMapper;

    private Usuario buildUsuario() {
        return Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .senha("hunter2")
                .build();
    }

    @Test
    public void criarUsuario_quandoValido_entaoSucesso() throws Exception {
        final Map<String, String> usuarioJson = new HashMap<>();
        usuarioJson.put("nome", "Fulano");
        usuarioJson.put("email", "fulano@example.com");
        usuarioJson.put("senha", "hunter2");

        mvc.perform(post("/usuarios")
                .content(objectMapper.writeValueAsString(usuarioJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Usuario usuarioCriado = usuarioService.findAll().get(0);
        assertThat(usuarioCriado.getNome(), equalTo(usuarioJson.get("nome")));
        assertThat(usuarioCriado.getEmail(), equalTo(usuarioJson.get("email")));
        assertThat(usuarioCriado.getSenha(), not(equalTo(usuarioJson.get("senha"))));
    }

    @Test
    public void criarUsuario_quandoInvalido_entaoErro() throws Exception {
        final Map<String, String> usuarioJson = new HashMap<>();
        usuarioJson.put("nome", "Fulano");

        mvc.perform(post("/usuarios")
                .content(objectMapper.writeValueAsString(usuarioJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void obterUsuario_quandoExiste_entaoRetorna() throws Exception {
        final Usuario usuario = usuarioService.save(buildUsuario());

        mvc.perform(get(String.format("/usuarios/%d", usuario.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", equalTo(usuario.getNome())));
    }

    @Test
    public void obterUsuario_quantoNaoExiste_entaoErro() throws Exception {
        mvc.perform(get("/usuarios/999"))
                .andExpect(status().isNotFound());
    }
}
