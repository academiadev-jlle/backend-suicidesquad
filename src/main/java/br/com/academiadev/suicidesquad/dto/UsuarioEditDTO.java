package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class UsuarioEditDTO {
    @ApiModelProperty(value = "Nome", example = "Fulano", required = true)
    @NotNull
    private String nome;

    @ApiModelProperty(value = "Sexo", allowableValues = "NAO_INFORMADO,MASCULINO,FEMININO")
    private String sexo;

    @ApiModelProperty(value = "Data de nascimento", example = "2018-01-01")
    private String data_nascimento;

    @ApiModelProperty(value = "Localização")
    private LocalizacaoDTO localizacao;

    @ApiModelProperty(value = "Telefones")
    private Set<TelefoneDTO> telefones;

    @ApiModelProperty(value = "Email visível nos anúncios?")
    private boolean email_publico;
}
