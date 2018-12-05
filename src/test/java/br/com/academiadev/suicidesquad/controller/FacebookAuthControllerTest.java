package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.service.FacebookService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.social.facebook.api.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "app.social.facebook.enabled=true",
        "app.social.facebook.frontend-redirect-uri=http://example.com/"
})
@AutoConfigureMockMvc
@Transactional
public class FacebookAuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private FacebookService facebookService;

    @Test
    public void getFacebookAuthorization() throws Exception {
        final String authorizationURL = "http://example.com/authorize?app=blah";
        when(facebookService.createAuthorizationURL()).thenReturn(authorizationURL);

        mvc.perform(get("/auth/facebook/authorization"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(authorizationURL));
    }

    @Test
    public void cadastrarViaCallback_quandoValidoComEmail_entaoRedirectionaComTokenSessao() throws Exception {
        final String facebookCode = "i'm a legit code. it's true!";
        final String facebookAccessToken = "i'm a valid access token, believe me folks.";
        final User facebookUser = mock(User.class);
        final Usuario usuario = mock(Usuario.class);
        when(facebookService.getAccessToken(facebookCode)).thenReturn(Optional.of(facebookAccessToken));
        when(facebookService.getUser(facebookAccessToken)).thenReturn(Optional.of(facebookUser));
        when(facebookService.buildUsuarioFromFacebookUser(facebookUser)).thenReturn(usuario);
        when(usuario.getEmail()).thenReturn("fulano@example.com");

        mvc.perform(get("/auth/facebook/cadastrar_e_logar_via_callback")
                .param("code", facebookCode))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://example.com/"))
                .andExpect(cookie().exists("token"));
    }

    @Test
    public void cadastrarViaCallback_quandoValidoSemEmail_entaoRedirecionaComAccessToken() throws Exception {
        final String facebookCode = "i'm a legit code. it's true!";
        final String facebookAccessToken = "i'm a valid access token, believe me folks.";
        final User facebookUser = mock(User.class);
        final Usuario usuario = mock(Usuario.class);
        when(facebookService.getAccessToken(facebookCode)).thenReturn(Optional.of(facebookAccessToken));
        when(facebookService.getUser(facebookAccessToken)).thenReturn(Optional.of(facebookUser));
        when(facebookService.buildUsuarioFromFacebookUser(facebookUser)).thenReturn(usuario);
        when(usuario.getEmail()).thenReturn(null);

        mvc.perform(get("/auth/facebook/cadastrar_e_logar_via_callback")
                .param("code", facebookCode))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://example.com/?accessToken=" + facebookAccessToken))
                .andExpect(cookie().doesNotExist("token"));
    }

    @Test
    public void cadastrarViaCallback_quandoInvalido_entaoRedirecionaSemTokenSessao() throws Exception {
        final String facebookCode = "i'm an invalid code. try me!";
        when(facebookService.getAccessToken(facebookCode)).thenReturn(Optional.empty());

        mvc.perform(get("/auth/facebook/cadastrar_e_logar_via_callback")
                .param("code", facebookCode))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://example.com/"))
                .andExpect(cookie().doesNotExist("token"));
    }

    @Test
    public void cadastrarComEmailSuplementar_quandoValido_entaoCadastraERetornaTokenSessao() throws Exception {
        final String facebookAccessToken = "i'm a valid access token, believe me folks.";
        final User facebookUser = mock(User.class);
        final Usuario usuario = Usuario.builder().nome("Fulano").build();
        when(facebookService.getUser(facebookAccessToken)).thenReturn(Optional.of(facebookUser));
        when(facebookService.buildUsuarioFromFacebookUser(facebookUser)).thenReturn(usuario);

        String emailSuplementar = "fulano@example.com";

        ObjectNode dto = objectMapper.createObjectNode();
        dto.put("access_token", facebookAccessToken);
        dto.put("email_suplementar", emailSuplementar);

        mvc.perform(post("/auth/facebook/cadastrar_e_logar_com_email_suplementar")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"));

        assertThat(usuarioService.existsByEmail(emailSuplementar), equalTo(true));
    }

    @Test
    public void cadastrarComEmailSuplementar_quandoUsuarioExistente_entaoRetornaErro() throws Exception {
        final String facebookAccessToken = "i'm a valid access token, believe me folks.";
        String facebookUserId = "123";

        final User facebookUser = mock(User.class);
        when(facebookUser.getId()).thenReturn(facebookUserId);
        when(facebookService.getUser(facebookAccessToken)).thenReturn(Optional.of(facebookUser));

        usuarioService.save(Usuario.builder()
                .nome("Fulano A")
                .email("fulano.a@example.com")
                .facebookUserId(facebookUserId)
                .build());

        ObjectNode dto = objectMapper.createObjectNode();
        dto.put("access_token", facebookAccessToken);
        dto.put("email_suplementar", "fulano.b@example.com");

        mvc.perform(post("/auth/facebook/cadastrar_e_logar_com_email_suplementar")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist("token"));
    }

    @Test
    public void loginComAccessToken_quandoValido_entaoRetornaTokenSessao() throws Exception {
        final String facebookAccessToken = "i'm a valid access token, believe me folks.";
        String facebookUserId = "123";

        final User facebookUser = mock(User.class);
        when(facebookUser.getId()).thenReturn(facebookUserId);
        when(facebookService.getUser(facebookAccessToken)).thenReturn(Optional.of(facebookUser));

        usuarioService.save(Usuario.builder()
                .email("fulano@example.com")
                .facebookUserId(facebookUserId)
                .nome("Fulano")
                .build());

        ObjectNode dto = objectMapper.createObjectNode();
        dto.put("access_token", facebookAccessToken);

        mvc.perform(post("/auth/facebook/logar_com_access_token")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"));
    }

    @Test
    public void loginComAccessToken_quandoInvalido_entaoRetornaErro() throws Exception {
        final String facebookAccessToken = "i'm a valid access token, believe me folks.";
        when(facebookService.getUser(facebookAccessToken)).thenReturn(Optional.empty());

        ObjectNode dto = objectMapper.createObjectNode();
        dto.put("access_token", facebookAccessToken);

        mvc.perform(post("/auth/facebook/logar_com_access_token")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist("token"));
    }
}