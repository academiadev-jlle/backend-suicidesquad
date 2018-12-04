package br.com.academiadev.suicidesquad.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AlterarSenhaDTO {
    @NotNull
    private String senha_atual;
    @NotNull
    private String senha_nova;
}
