package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.email.EmailMessage;
import br.com.academiadev.suicidesquad.email.NullEmailTransport;
import br.com.academiadev.suicidesquad.entity.Usuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmailService.class})
@TestPropertySource(properties = {
        "app.email.provider=null",
        "app.email.sender.domain=404pets.com",
        "app.email.mailtrap.username=27dcff65ac480e",
        "app.email.mailtrap.password=5d423aa4109278",
})
public class EmailServiceTest {

    @Autowired
    private Environment environment;

    @Test
    public void sendEmail() {
        Usuario usuario = Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .build();

        NullEmailTransport transport = mock(NullEmailTransport.class);
        EmailService emailService = new EmailService(environment, transport);

        emailService.enviarParaUsuario(
                usuario,
                "Email de teste",
                "Olá, Fulano. Você foi escolhido para fazer parte dos nossos testes de unidade. Parabéns.");

        ArgumentCaptor<EmailMessage> captor = ArgumentCaptor.forClass(EmailMessage.class);
        verify(transport, times(1)).enviar(captor.capture());
        EmailMessage captured = captor.getValue();

        assertThat(captured.getDestinatario(), equalTo(usuario.getEmail()));
        assertThat(captured.getAssunto(), equalTo("Email de teste"));
    }
}