package br.com.academiadev.suicidesquad.email;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailtrapEmailTransport implements EmailTransport {
    private JavaMailSenderImpl sender;

    public MailtrapEmailTransport(String host, int port, String username, String password) {
        sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
    }

    @Override
    public void enviar(EmailMessage mensagem) {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setFrom(mensagem.getRemetente());
            helper.setTo(mensagem.getDestinatario());
            helper.setSubject(mensagem.getAssunto());
            helper.setText(mensagem.getConteudo(), true);
        } catch (MessagingException e) {
            // TODO: handle email error
            throw new RuntimeException("Could not send email: " + e.toString());
        }

        sender.send(mimeMessage);
    }
}
