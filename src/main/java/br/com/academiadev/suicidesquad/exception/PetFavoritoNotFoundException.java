package br.com.academiadev.suicidesquad.exception;

import br.com.academiadev.suicidesquad.exception.ResourceNotFoundException;

public class PetFavoritoNotFoundException extends ResourceNotFoundException {

    @Override
    public String getMessage() {
        return "Pet Favorito não encontrado";
    }

}
