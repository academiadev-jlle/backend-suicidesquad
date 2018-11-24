package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistroDTO {
    @ApiModelProperty(value = "Situação", example = "ENCONTRADO")
    private String situacao;

    @ApiModelProperty(value = "Data de cadastro da situação", example = "2018-01-01 00:00:00")
    private String data;
}
