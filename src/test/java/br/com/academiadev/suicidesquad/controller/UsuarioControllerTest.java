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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private PetService petService;

    @Autowired
    ObjectMapper objectMapper;

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
        Usuario usuarioExistente = usuarioService.save(buildUsuario());

        final Map<String, String> usuarioNovoJson = new HashMap<>();
        usuarioNovoJson.put("nome", "Fulano");
        usuarioNovoJson.put("email", usuarioExistente.getEmail());
        usuarioNovoJson.put("senha", "hunter2");

        mvc.perform(post("/usuarios")
                .content(objectMapper.writeValueAsString(usuarioNovoJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void criarUsuario_quandoEmailExistente_entaoErro() throws Exception {
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
                .andExpect(jsonPath("$.nome", equalTo(usuario.getNome())))
                .andExpect(jsonPath("$.email", equalTo(usuario.getEmail())));
    }

    @Test
    public void obterUsuario_quantoNaoExiste_entaoErro() throws Exception {
        mvc.perform(get("/usuarios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletarUsuario_quantoAutorizado_entaoSucesso() throws Exception {
        final Usuario usuario = usuarioService.save(buildUsuario());

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(delete(String.format("/usuarios/%d", usuario.getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertThat(usuarioService.findAll(), hasSize(0));
    }

    @Test
    public void deletarUsuario_quantoNaoAutorizado_entaoErro() throws Exception {
        final Usuario usuarioA = usuarioService.save(Usuario.builder()
                .nome("Usuário A")
                .email("usuario.a@example.com")
                .senha("hunter2")
                .build());
        final Usuario usuarioB = usuarioService.save(Usuario.builder()
                .nome("Usuário B")
                .email("usuario.b@example.com")
                .senha("hunter2")
                .build());

        String tokenA = jwtTokenProvider.getToken(usuarioA.getUsername(), Collections.emptyList());
        mvc.perform(delete(String.format("/usuarios/%d", usuarioB.getId()))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());

        assertThat(usuarioService.findAll(), hasSize(2));
    }

    @Test
    public void obterUsuario_quandoExisteEmailNaoPublico_entaoRetornaEmailNull() throws Exception {
        final Usuario usuario = buildUsuario();
        usuario.setEmailPublico(false);
        usuarioService.save(usuario);

        mvc.perform(get(String.format("/usuarios/%d", usuario.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", equalTo(usuario.getNome())))
                .andExpect(jsonPath("$.email", equalTo(null)));
    }

    @Test
    public void editarUsuario_quandoValidoEAutorizado_entaoSucesso() throws Exception {
        final Usuario usuario = usuarioService.save(buildUsuario());
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.PEQUENO)
                .categoria(Categoria.PERDIDO)
                .comprimentoPelo(ComprimentoPelo.LONGO)
                .build());
        usuario.addPet(pet);

        String usuarioJson = "{" +
                "   \"nome\": \"Novo nome\"," +
                "   \"telefones\": [" +
                "       {" +
                "           \"numero\": \"(47) 99999-9999\"" +
                "       }" +
                "   ]" +
                "}";
        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(put(String.format("/usuarios/%d", usuario.getId()))
                .header("Authorization", "Bearer " + token)
                .content(usuarioJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(usuario.getNome(), equalTo("Novo nome"));
        assertThat(usuario.getEmail(), equalTo("fulano@example.com"));
        assertThat(usuario.getTelefones().get(0).getNumero(), equalTo("(47) 99999-9999"));
        assertThat(usuario.getTelefones().get(0).getUsuario().getId(), equalTo(usuario.getId()));
        assertThat(usuario.getPets().get(0).getId(), equalTo(pet.getId()));
        assertThat(usuario.getPets().get(0).getUsuario().getId(), equalTo(usuario.getId()));
    }

    @Test
    public void editarUsuario_quandoInvalidoEAutorizado_entaoErro() throws Exception {
        final Usuario usuario = usuarioService.save(buildUsuario());

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(put(String.format("/usuarios/%d", usuario.getId()))
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString("{}"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editarUsuario_quandoNaoAutorizado_entaoErro() throws Exception {
        final Usuario usuarioA = usuarioService.save(Usuario.builder()
                .nome("Usuário A")
                .email("usuario.a@example.com")
                .senha("hunter2")
                .build());
        final Usuario usuarioB = usuarioService.save(Usuario.builder()
                .nome("Usuário B")
                .email("usuario.b@example.com")
                .senha("hunter2")
                .build());

        Map<String, String> usuarioJson = new HashMap<>();
        usuarioJson.put("nome", "Novo nome");

        String tokenA = jwtTokenProvider.getToken(usuarioA.getUsername(), Collections.emptyList());
        mvc.perform(put(String.format("/usuarios/%d", usuarioB.getId()))
                .header("Authorization", "Bearer " + tokenA)
                .content(objectMapper.writeValueAsString(usuarioJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
