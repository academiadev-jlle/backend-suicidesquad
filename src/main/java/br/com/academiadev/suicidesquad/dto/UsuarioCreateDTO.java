package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UsuarioCreateDTO {
    @ApiModelProperty(value = "Nome", example = "Fulano", required = true)
    @NotNull
    private String nome;

    @ApiModelProperty(value = "E-mail", example = "fulano@example.com", required = true)
    @NotNull
    private String email;

    @ApiModelProperty(value = "Senha", required = true)
    @NotNull
    private String senha;

    @ApiModelProperty(value = "Sexo", allowableValues = "NAO_INFORMADO,MASCULINO,FEMININO")
    private String sexo;

    @ApiModelProperty(value = "Data de nascimento", example = "2018-01-01")
    private String data_nascimento;

    @ApiModelProperty(value = "Localização")
    private LocalizacaoDTO localizacao;
}
