package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class TelefoneDTO {
    @ApiModelProperty(value = "Número", example = "(47) 99999-9999")
    @NotNull
    private String numero;

    @ApiModelProperty(value = "Tem WhatsApp?", example = "true")
    private boolean whatsapp;
}
