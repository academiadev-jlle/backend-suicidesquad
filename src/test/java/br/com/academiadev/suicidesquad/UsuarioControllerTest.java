package br.com.academiadev.suicidesquad;

import br.com.academiadev.suicidesquad.controller.UsuarioController;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(UsuarioController.class)
@EnableSpringDataWebSupport
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioService usuarioService;


    private Usuario buildUsuario() {
        Telefone telefone = new Telefone();
        telefone.setNumero("47988271821");
        telefone.setWhatsapp(true);

        Usuario usuario1 = new Usuario();
        usuario1.setEmail("primeiroemail@gmail.com");
        usuario1.setNome("Nelson Nunes Guimarães");
        usuario1.setSenha("senha1");
        usuario1.setId(1l);
        usuario1.addTelefone(telefone);
        usuario1.setLocalizacao(new Localizacao());
        usuario1.setDataNascimento(LocalDate.of(2000, 5, 22));
        return usuario1;
    }

    private List<Usuario> buildUsuarios() {
        Telefone telefone = new Telefone();
        telefone.setNumero("47988271821");
        telefone.setWhatsapp(true);

        Usuario usuario1 = new Usuario();
        usuario1.setEmail("primeiroemail@gmail.com");
        usuario1.setNome("Nelson Nunes Guimarães");
        usuario1.setSenha("senha1");
        usuario1.setId(1l);
        usuario1.addTelefone(telefone);
        usuario1.setLocalizacao(new Localizacao());
        usuario1.setDataNascimento(LocalDate.of(1965, 5, 16));

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("segundoemail@gmail.com");
        usuario2.setNome("Salvador Di Bernardi");
        usuario2.setId(2l);
        usuario2.setSenha("senha2");
        usuario2.addTelefone(telefone);
        usuario2.setLocalizacao(new Localizacao());
        usuario2.setDataNascimento(LocalDate.of(1983, 11, 28));

        return Arrays.asList(usuario1,
                usuario2);
    }

    @Test
    public void dadoBuscarUsuarios_quandoExistemUSuarios_entaoUsuariosEncontrados() throws Exception {

        List<Usuario> allUsuarios = buildUsuarios();

        Page<Usuario> pagedResponse = new PageImpl<>(allUsuarios);

        when(usuarioService.findAll(any(Pageable.class))).thenReturn(pagedResponse);

        this.mvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email", is("primeiroemail@gmail.com")))
                .andExpect(jsonPath("$.content[0].nome", is("Nelson Nunes Guimarães")))
                .andExpect(jsonPath("$.content[1].nome", is("Salvador Di Bernardi")));
    }

    @Test
    public void dadoBuscarUsuario_quandoUsuarioExiste_entaoUsuarioEncontrado() throws Exception {
        Usuario usuario1 = buildUsuario();

        when(usuarioService.findById(1l)).thenReturn(java.util.Optional.of(usuario1));

        this.mvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Nelson Nunes Guimarães")));


    }

    @Test
    public void dadoBuscarUsuarios_quandoNaoExistemUsuarios_entaoRetornaListaVazia() throws Exception {
        Page<Usuario> pagedResponde = new PageImpl<>(new ArrayList<>());
        when(usuarioService.findAll(any(Pageable.class))).thenReturn(pagedResponde);

        this.mvc.perform(get("/usuarios")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));


    }

    @Test
    public void dadoBuscarUsuario_quandoUsuarioNaoExiste_entaoUsuarioNaoEncontrado() throws Exception {
        when(usuarioService.findById(1l)).thenReturn(Optional.empty());

        this.mvc.perform(get("/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void dadoCriarUsuario_quandoUsuarioInexistente_entaoUsuarioCriado() throws Exception {

        JSONObject usuarioJson = new JSONObject();
        usuarioJson.put("nome", "Nelson Nunes Guimarães");
        usuarioJson.put("email", "primeiroemail@gmail.com");
        usuarioJson.put("senha", "senha1");
        usuarioJson.put("sexo", "MASCULINO");
        usuarioJson.put("dataNascimento", LocalDate.of(1990, 10, 8));

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
                .andExpect(status().isOk());

        ArgumentCaptor<Usuario> argumentCaptor = ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioService, times(1)).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getNome(), equalTo(usuarioJson.getString("nome")));
        assertThat(argumentCaptor.getValue().getSenha(), equalTo(usuarioJson.getString("senha")));
        assertThat(argumentCaptor.getValue().getEmail(), equalTo(usuarioJson.getString("email")));
        assertThat(argumentCaptor.getValue().getTelefones().get(0).getNumero(), equalTo(telefoneJson.getString("numero")));

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
