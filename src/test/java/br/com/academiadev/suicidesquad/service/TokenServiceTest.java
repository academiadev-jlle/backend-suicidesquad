package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Usuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TokenService.class})
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    private Usuario buildUsuario() {
        final Usuario usuario = Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .senha("hunter2")
                .build();
        usuario.setCreatedDate(LocalDateTime.now());
        return usuario;
    }

    @Test
    public void generateRedefinicaoSenhaToken_geraTokensValidos() {
        Usuario usuario = buildUsuario();
        final String token = tokenService.generateRedefinicaoSenhaToken(usuario);

        assertThat(tokenService.validateRedefinicaoSenhaToken(usuario, token), equalTo(true));
    }

    @Test
    public void validateRedefinicaoSenhaToken_quandoTokenInvalido_retornaFalse() {
        Usuario usuario = buildUsuario();
        assertThat(tokenService.validateRedefinicaoSenhaToken(usuario, "um token errado"), equalTo(false));
    }
}