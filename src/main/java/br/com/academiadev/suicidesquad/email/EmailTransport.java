package br.com.academiadev.suicidesquad.email;

public interface EmailTransport {
    void enviar(EmailMessage mensagem);
}
