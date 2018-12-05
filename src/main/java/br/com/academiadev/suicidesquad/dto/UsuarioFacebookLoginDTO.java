package br.com.academiadev.suicidesquad.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsuarioFacebookLoginDTO {
    @NotNull
    private String access_token;
}
