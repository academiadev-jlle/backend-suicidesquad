package br.com.academiadev.suicidesquad.exception;

public class PetNotFoundException extends ResourceNotFoundException {

    @Override
    public String getMessage() {
        return "Pet não encontrado";
    }
}
