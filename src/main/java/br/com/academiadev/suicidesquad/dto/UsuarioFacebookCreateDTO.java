package br.com.academiadev.suicidesquad.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UsuarioFacebookCreateDTO {
    @NotNull
    private String access_token;

    @NotNull
    @Email
    private String email_suplementar;
}
