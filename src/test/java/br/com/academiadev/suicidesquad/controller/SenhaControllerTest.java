package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import br.com.academiadev.suicidesquad.service.EmailService;
import br.com.academiadev.suicidesquad.service.PasswordService;
import br.com.academiadev.suicidesquad.service.TokenService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SenhaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private EmailService emailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private String sessionToken;

    @Before
    public void setUp() {
        usuario = usuarioService.save(Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .senha(PasswordService.encoder().encode("senha1"))
                .build());
        sessionToken = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
    }

    @Test
    public void alterarSenha_quandoSenhaAtualCorreta_entaoSucesso() throws Exception {
        String oldHash = usuario.getSenha();

        ObjectNode alterarSenhaJson = objectMapper.createObjectNode();
        alterarSenhaJson.put("senha_atual", "senha1");
        alterarSenhaJson.put("senha_nova", "senha2");

        mvc.perform(put("/senhas/alterar")
                .content(objectMapper.writeValueAsString(alterarSenhaJson))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + sessionToken))
                .andExpect(status().isOk());

        assertThat(usuario.getSenha(), not(equalTo(oldHash)));
    }

    @Test
    public void alterarSenha_quandoSenhaAtualIncorreta_entaoErro() throws Exception {
        String oldHash = usuario.getSenha();

        ObjectNode alterarSenhaJson = objectMapper.createObjectNode();
        alterarSenhaJson.put("senha_atual", "senha errada");
        alterarSenhaJson.put("senha_nova", "senha2");

        mvc.perform(put("/senhas/alterar")
                .content(objectMapper.writeValueAsString(alterarSenhaJson))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + sessionToken))
                .andExpect(status().isUnauthorized());

        assertThat(usuario.getSenha(), equalTo(oldHash));
    }

    @Test
    public void redefinirSenha_quandoTokenValido_entaoSucesso() throws Exception {
        String oldHash = usuario.getSenha();

        mvc.perform(put("/senhas/requisitar_redefinicao")
                .content("{\"email\": \"fulano@example.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String tokenRedefinicao = tokenService.generateRedefinicaoSenhaToken(usuario);
        verify(emailService, times(1))
                .enviarParaUsuario(eq(usuario), any(String.class), contains(tokenRedefinicao));

        ObjectNode redefinirSenhaJson = objectMapper.createObjectNode();
        redefinirSenhaJson.put("token", tokenRedefinicao);
        redefinirSenhaJson.put("senha_nova", "senha3");

        mvc.perform(put("/senhas/redefinir")
                .content(objectMapper.writeValueAsString(redefinirSenhaJson))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + sessionToken))
                .andExpect(status().isOk());

        assertThat(usuario.getSenha(), not(equalTo(oldHash)));
    }

    @Test
    public void redefinirSenha_quandoTokenInvalido_entaoErro() throws Exception {
        String oldHash = usuario.getSenha();

        ObjectNode redefinirSenhaJson = objectMapper.createObjectNode();
        redefinirSenhaJson.put("token", "token inválido");
        redefinirSenhaJson.put("senha_nova", "senha3");

        mvc.perform(put("/senhas/redefinir")
                .content(objectMapper.writeValueAsString(redefinirSenhaJson))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + sessionToken))
                .andExpect(status().isBadRequest());

        assertThat(usuario.getSenha(), equalTo(oldHash));
    }
}