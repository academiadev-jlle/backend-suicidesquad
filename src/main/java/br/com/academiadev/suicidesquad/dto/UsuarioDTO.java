package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    @ApiModelProperty(value = "ID", example = "1")
    private String id;

    @ApiModelProperty(value = "Data de criação", example = "2018-01-01 00:00:00")
    private String data_criacao;

    @ApiModelProperty(value = "Nome", example = "Fulano")
    private String nome;

    @ApiModelProperty(value = "email", example = "fulano@example.com")
    private String email;
}
