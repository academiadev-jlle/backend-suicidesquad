package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.email.*;
import br.com.academiadev.suicidesquad.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.email.sender.domain:}")
    private String SENDER_DOMAIN;

    @Value("${app.email.sender.default-name:nao-responda}")
    private String SENDER_DEFAULT_USER;

    private final Environment env;

    private EmailTransport transport;

    @Autowired
    public EmailService(Environment env) {
        this.env = env;
        this.transport = buildTransport();
    }

    // Construtor usado nos testes para injetar o mock de transport
    public EmailService(Environment env, EmailTransport transport) {
        this.env = env;
        this.transport = transport;
    }

    private EmailTransport buildTransport() {
        String provider = env.getProperty("app.email.provider", "null");
        if (provider.equals("sendgrid")) {
            String sendGridApiKey = env.getProperty("app.email.sendgrid.api-key");
            if (sendGridApiKey == null || sendGridApiKey.isEmpty()) {
                throw new RuntimeException("Can't send email: SendGrid API key not configured.");
            }
            return new SendGridEmailTransport(sendGridApiKey);
        } else if (provider.equals("mailtrap")) {
            String host = env.getProperty("app.email.mailtrap.host", "smtp.mailtrap.io");
            String port = env.getProperty("app.email.mailtrap.port", "2525");
            String username = env.getProperty("app.email.mailtrap.username");
            String password = env.getProperty("app.email.mailtrap.password");
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                throw new RuntimeException("Can't send email: Mailtrap credentials not configured.");
            }
            return new MailtrapEmailTransport(host, Integer.valueOf(port), username, password);
        } else {
            return new NullEmailTransport();
        }
    }

    private String getEmailRemetente(String username) {
        return String.format("%s@%s", username, getSenderDomain());
    }

    private String getSenderDomain() {
        if (transport instanceof NullEmailTransport) {
            return "example.com";
        } else if (SENDER_DOMAIN != null && !SENDER_DOMAIN.isEmpty()) {
            return SENDER_DOMAIN;
        } else {
            throw new RuntimeException("Can't send email: Sender domain not configured.");
        }
    }

    public void enviarParaUsuario(Usuario destinatario, String assunto, String conteudo, String usernameRemetente) {
        transport.enviar(new EmailMessage(
                getEmailRemetente(usernameRemetente),
                destinatario.getEmail(),
                assunto,
                conteudo
        ));
    }

    public void enviarParaUsuario(Usuario destinatario, String assunto, String conteudo) {
        enviarParaUsuario(destinatario, assunto, conteudo, SENDER_DEFAULT_USER);
    }
}
