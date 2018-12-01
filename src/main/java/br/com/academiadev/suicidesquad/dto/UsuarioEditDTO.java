package br.com.academiadev.suicidesquad.dto;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioEditDTO {
    @ApiModelProperty(value = "Nome", example = "Fulano", required = true)
    @NotNull
    private String nome;

    @ApiModelProperty(value = "E-mail", example = "fulano@example.com", required = true)
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(value = "Senha")
    private String senha;

    @ApiModelProperty(value = "Sexo", allowableValues = "NAO_INFORMADO,MASCULINO,FEMININO")
    private String sexo;

    @ApiModelProperty(value = "Data de nascimento", example = "2018-01-01")
    private String data_nascimento;

    @ApiModelProperty(value = "Localização")
    private LocalizacaoDTO localizacao;

    @ApiModelProperty(value = "Telefones")
    private Set<TelefoneDTO> telefones;
    
    @ApiModelProperty(value = "Fotos", example = "link_do_repositório_externo")
    private List<String> fotos;
}
