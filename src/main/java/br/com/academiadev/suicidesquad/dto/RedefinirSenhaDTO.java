package br.com.academiadev.suicidesquad.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RedefinirSenhaDTO {
    @NotNull
    private String token;
    @NotNull
    private String senha_nova;
}
