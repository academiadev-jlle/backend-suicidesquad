package br.com.academiadev.suicidesquad.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EmailTransportFactory.class})
public class EmailTransportFactoryTest {

    @Autowired
    EmailTransportFactory factory;

    @MockBean
    private Environment env;

    private void mockProviderProperty(String provider) {
        when(env.getProperty(eq("app.email.provider"), anyString())).thenReturn(provider);
    }

    @Test
    public void construirNullTransport() {
        mockProviderProperty("null");
        assertThat(factory.buildTransport(), instanceOf(NullEmailTransport.class));
    }

    @Test(expected = RuntimeException.class)
    public void construirMailtrapTransport_quandoCredenciaisInvalidas_retornaErro() {
        mockProviderProperty("mailtrap");
        factory.buildTransport();
    }

    @Test
    public void construirMailtrapTransport_quandoCredenciaisValidas_retornaTransport() {
        mockProviderProperty("mailtrap");
        when(env.getProperty(eq("app.email.mailtrap.host"), anyString())).thenReturn("example.com");
        when(env.getProperty(eq("app.email.mailtrap.port"), anyString())).thenReturn("1337");
        when(env.getProperty("app.email.mailtrap.username")).thenReturn("test-username");
        when(env.getProperty("app.email.mailtrap.password")).thenReturn("test-password");
        assertThat(factory.buildTransport(), instanceOf(MailtrapEmailTransport.class));
    }

    @Test(expected = RuntimeException.class)
    public void construirSendGridTransport_quandoAPIKeyInvalida_retornaErro() {
        mockProviderProperty("sendgrid");
        factory.buildTransport();
    }

    @Test
    public void construirSendGridTransport_quandoAPIKeyValida_retornaErro() {
        mockProviderProperty("sendgrid");
        when(env.getProperty("app.email.sendgrid.api-key")).thenReturn("test-api-key");
        assertThat(factory.buildTransport(), instanceOf(SendGridEmailTransport.class));
    }
}