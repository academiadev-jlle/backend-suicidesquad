package br.com.academiadev.suicidesquad.email;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailMessage {
    private String remetente;
    private String destinatario;
    private String assunto;
    private String conteudo;
}
