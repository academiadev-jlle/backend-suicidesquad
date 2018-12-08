package br.com.academiadev.suicidesquad.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PetCreateDTO {
    @ApiModelProperty(value = "Tipo", allowableValues = "CACHORRO,GATO,EQUINO", required = true)
    @NotNull
    private String tipo;

    @ApiModelProperty(value = "Porte", allowableValues = "PEQUENO,MEDIO,GRANDE", required = true)
    @NotNull
    private String porte;

    @ApiModelProperty(value = "Raca", example = "Labrador")
    private String raca;

    @ApiModelProperty(value = "Comprimento do pelo", allowableValues = "SEM_PELO,CURTO,MEDIO,LONGO", required = true)
    @NotNull
    private String comprimento_pelo;

    @ApiModelProperty(value = "Sexo", allowableValues = "NAO_INFORMADO,MASCULINO,FEMININO")
    private String sexo;

    @ApiModelProperty(value = "Categoria", allowableValues = "ACHADO,PERDIDO,PARA_ADOCAO", required = true)
    @NotNull
    private String categoria;

    @ApiModelProperty(value = "Vacinação", allowableValues = "NAO_INFORMADO,PARCIAL,EM_DIA")
    private String vacinacao;

    @ApiModelProperty(value = "Castração", allowableValues = "NAO_INFORMADO,NAO_CASTRADO,CASTRADO")
    private String castracao;

    @ApiModelProperty(value = "Nome", example = "Bidu")
    @Size(min = 2, max = 80)
    private String nome;

    @ApiModelProperty(value = "Cores", example = "MARROM")
    private List<String> cores;

    @ApiModelProperty(value = "Localização")
    private LocalizacaoDTO localizacao;

    @ApiModelProperty(value = "Descrição")
    @Size(min = 1, max = 200)
    private String descricao;
}
