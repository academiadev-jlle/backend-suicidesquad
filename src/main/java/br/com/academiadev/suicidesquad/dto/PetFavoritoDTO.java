package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PetFavoritoDTO {

    @ApiModelProperty(value = "Pets favoritos do usuário")
    private PetDTO pet;

    @ApiModelProperty(value = "Data de criação", example = "2018-01-01 00:00:00")
    private String data_criacao;
}
