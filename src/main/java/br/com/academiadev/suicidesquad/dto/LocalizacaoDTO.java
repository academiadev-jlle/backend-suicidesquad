package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocalizacaoDTO {
    @ApiModelProperty(value = "Bairro", example = "Centro")
    private String bairro;

    @ApiModelProperty(value = "Cidade", example = "São Paulo")
    private String cidade;

    @ApiModelProperty(value = "UF", example = "São Paulo")
    private String uf;
}
