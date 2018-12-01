package br.com.academiadev.suicidesquad.exception;

public class UsuarioNotFoundException extends ResourceNotFoundException {

    @Override
    public String getMessage() {
        return "Usuário não encontrado";
    }
}
