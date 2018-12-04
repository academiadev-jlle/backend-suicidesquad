package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.email.EmailMessage;
import br.com.academiadev.suicidesquad.email.EmailTransport;
import br.com.academiadev.suicidesquad.email.EmailTransportFactory;
import br.com.academiadev.suicidesquad.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private String senderDomain;

    private String senderDefaultUser;

    private final EmailTransport transport;

    @Autowired
    public EmailService(EmailTransportFactory transportFactory, @Value("${app.email.sender.domain}") String senderDomain, @Value("${app.email.sender.default-name:nao-responder}") String senderDefaultUser) {
        this.transport = transportFactory.buildTransport();
        this.senderDomain = senderDomain;
        this.senderDefaultUser = senderDefaultUser;
    }

    private String getEmailRemetente(String username) {
        return String.format("%s@%s", username, this.senderDomain);
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
        enviarParaUsuario(destinatario, assunto, conteudo, this.senderDefaultUser);
    }
}
