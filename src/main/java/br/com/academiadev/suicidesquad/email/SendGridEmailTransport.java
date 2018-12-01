package br.com.academiadev.suicidesquad.email;

import com.sendgrid.*;

import java.io.IOException;

public class SendGridEmailTransport implements EmailTransport {

    private SendGrid sendGrid;

    public SendGridEmailTransport(String apiKey) {
        sendGrid = new SendGrid(apiKey);
    }

    public void enviar(EmailMessage mensagem) {
        Mail mail = new Mail(
                new Email(mensagem.getRemetente()),
                mensagem.getAssunto(),
                new Email(mensagem.getDestinatario()),
                new Content(mensagem.getConteudo(), "text/html")
        );
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        try {
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException e) {
            // TODO: handle email exception
            e.printStackTrace();
        }
    }
}
