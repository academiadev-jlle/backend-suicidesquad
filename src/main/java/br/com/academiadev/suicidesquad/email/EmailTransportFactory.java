package br.com.academiadev.suicidesquad.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EmailTransportFactory {
    private final Environment env;

    @Autowired
    public EmailTransportFactory(Environment env) {
        this.env = env;
    }

    public EmailTransport buildTransport() {
        String provider = env.getProperty("app.email.provider", "null");
        if (provider.equals("sendgrid")) {
            String sendGridApiKey = env.getProperty("app.email.sendgrid.api-key");
            if (sendGridApiKey == null || sendGridApiKey.isEmpty()) {
                throw new RuntimeException("Can't send email: SendGrid API key not configured.");
            }
            return new SendGridEmailTransport(sendGridApiKey);
        }
        if (provider.equals("mailtrap")) {
            String host = env.getProperty("app.email.mailtrap.host", "smtp.mailtrap.io");
            String port = env.getProperty("app.email.mailtrap.port", "2525");
            String username = env.getProperty("app.email.mailtrap.username");
            String password = env.getProperty("app.email.mailtrap.password");
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                throw new RuntimeException("Can't send email: Mailtrap credentials not configured.");
            }
            return new MailtrapEmailTransport(host, Integer.valueOf(port), username, password);
        }
        return new NullEmailTransport();
    }
}
