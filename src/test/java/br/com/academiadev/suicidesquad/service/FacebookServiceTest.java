package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.SexoUsuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.social.facebook.api.User;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {
                FacebookService.class
        },
        properties = {
                "app.social.facebook.app-id=test-app-id",
                "app.social.facebook.app-secret=test-app-secret",
                "app.social.facebook.backend-redirect-uri=http://example.com"
        }
)
public class FacebookServiceTest {

    @Autowired
    private FacebookService facebookService;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void buildUsuarioFromFacebookUser_quandoUsuarioExiste_entaoRetornaExistente() {
        String facebookUserId = "123";
        User facebookUser = mock(User.class);
        when(facebookUser.getId()).thenReturn(facebookUserId);

        Usuario usuario = mock(Usuario.class);
        when(usuarioService.findByFacebookUserId(facebookUserId)).thenReturn(Optional.of(usuario));

        assertThat(facebookService.buildUsuarioFromFacebookUser(facebookUser), equalTo(usuario));
    }

    @Test
    public void buildUsuarioFromFacebookUser_quandoUsuarioNaoExiste_entaoConstroi() {
        User facebookUser = new User(
                "123",
                "Fulano de Tal",
                "Fulano",
                "de Tal",
                "unknown",
                Locale.forLanguageTag("pt_BR")
        );

        Usuario usuario = facebookService.buildUsuarioFromFacebookUser(facebookUser);
        assertThat(usuario.getFacebookUserId(), equalTo("123"));
        assertThat(usuario.getNome(), equalTo("Fulano de Tal"));
        assertThat(usuario.getSexo(), equalTo(SexoUsuario.NAO_INFORMADO));
    }

    @Test
    public void createAuthorizationURL() {
        String authorizationURL = facebookService.createAuthorizationURL();
        assertThat(authorizationURL, startsWith("https://www.facebook.com/"));
        assertThat(authorizationURL, containsString("test-app-id"));
    }
}