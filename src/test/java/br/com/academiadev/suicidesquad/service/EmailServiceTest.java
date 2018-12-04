package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.email.EmailMessage;
import br.com.academiadev.suicidesquad.email.EmailTransport;
import br.com.academiadev.suicidesquad.email.EmailTransportFactory;
import br.com.academiadev.suicidesquad.entity.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EmailService.class})
public class EmailServiceTest {
    @MockBean
    private EmailTransportFactory transportFactory;

    @Mock
    private EmailTransport transport;

    private EmailService emailService;

    @Before
    public void setUp() {
        when(transportFactory.buildTransport()).thenReturn(transport);
        this.emailService = new EmailService(transportFactory, "404pets.com", "nao-responder");
    }

    @Test
    public void sendEmail() {
        Usuario usuario = Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .build();

        emailService.enviarParaUsuario(
                usuario,
                "Email de teste",
                "Olá, Fulano. Você foi escolhido para fazer parte dos nossos testes de unidade. Parabéns.");

        ArgumentCaptor<EmailMessage> captor = ArgumentCaptor.forClass(EmailMessage.class);
        verify(this.transport, times(1)).enviar(captor.capture());
        EmailMessage captured = captor.getValue();

        assertThat(captured.getDestinatario(), equalTo(usuario.getEmail()));
        assertThat(captured.getAssunto(), equalTo("Email de teste"));
    }
}
