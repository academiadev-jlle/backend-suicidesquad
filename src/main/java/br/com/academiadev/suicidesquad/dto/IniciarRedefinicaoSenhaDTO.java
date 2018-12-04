package br.com.academiadev.suicidesquad.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IniciarRedefinicaoSenhaDTO {
    @NotNull
    private String email;
}
