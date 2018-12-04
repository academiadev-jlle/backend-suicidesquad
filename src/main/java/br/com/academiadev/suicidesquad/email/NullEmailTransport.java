package br.com.academiadev.suicidesquad.email;

public class NullEmailTransport implements EmailTransport {
    @Override
    public void enviar(EmailMessage mensagem) {
        // Não faz nada!
    }
}
