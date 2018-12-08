package br.com.academiadev.suicidesquad.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioCreateDTO {
    @ApiModelProperty(value = "Nome", example = "Fulano", required = true)
    @NotNull
    @Size(min = 1, max = 120)
    private String nome;

    @ApiModelProperty(value = "E-mail", example = "fulano@example.com", required = true)
    @NotNull
    @Email
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

    @ApiModelProperty(value = "email visível nos anúncios")
    private boolean email_publico;
}
