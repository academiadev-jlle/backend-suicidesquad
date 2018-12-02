package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.InvalidTokenRedefinicaoSenhaException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedefinicaoSenhaService.class})
public class RedefinicaoSenhaServiceTest {
    @MockBean
    private EmailService emailService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private RedefinicaoSenhaService redefinicaoSenhaService;

    private Usuario buildUsuario() {
        final Usuario usuario = Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .senha("senha1")
                .build();
        usuario.setCreatedDate(LocalDateTime.now());
        return usuario;
    }

    @Test
    public void dadoIniciarRedefinicao_entaoPersistirToken() {
        final Usuario usuario = buildUsuario();
        final String token = "um token bem seguro";
        when(tokenService.generateRedefinicaoSenhaToken(usuario)).thenReturn(token);
        when(usuarioService.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        redefinicaoSenhaService.iniciarRedefinicao(usuario);

        verify(emailService, times(1))
                .enviarParaUsuario(eq(usuario), any(String.class), eq(token));
    }

    @Test
    public void dadoCompletarRedefinicao_quandoTokenValido_entaoAlterarSenhaERemoverToken() {
        final Usuario usuario = buildUsuario();
        final String senhaAntiga = usuario.getSenha();

        final String token = "um token bem seguro";
        when(tokenService.validateRedefinicaoSenhaToken(usuario, token)).thenReturn(true);
        redefinicaoSenhaService.completarRedefinicao(usuario, token, "senha2");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioService, times(1)).save(captor.capture());
        Usuario captured = captor.getValue();

        assertThat(captured.getSenha(), not(senhaAntiga));
    }

    @Test(expected = InvalidTokenRedefinicaoSenhaException.class)
    public void dadoCompletarRedefinicao_quantoTokenInvalido_entaoErro() {
        final Usuario usuario = buildUsuario();
        final String token = "um token errado";
        when(tokenService.validateRedefinicaoSenhaToken(usuario, token)).thenReturn(false);

        redefinicaoSenhaService.completarRedefinicao(usuario, token, "senha2");
    }
}