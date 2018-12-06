package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PetDTO {
    @ApiModelProperty(value = "Data de criação", example = "2018-01-01 00:00:00")
    private String data_criacao;

    @ApiModelProperty(value = "Data de edição", example = "2018-01-01 00:00:00")
    private String data_edicao;

    @ApiModelProperty(value = "Tipo", example = "CACHORRO")
    private String tipo;

    @ApiModelProperty(value = "Porte", example = "PEQUENO")
    private String porte;

    @ApiModelProperty(value = "Raca", example = "Labrador")
    private String raca;

    @ApiModelProperty(value = "Comprimento do pelo", example = "SEM_PELO")
    private String comprimento_pelo;

    @ApiModelProperty(value = "Sexo", example = "NAO_INFORMADO")
    private String sexo;

    @ApiModelProperty(value = "Categoria", example = "ACHADO")
    private String categoria;

    @ApiModelProperty(value = "Vacinação", example = "NAO_INFORMADO")
    private String vacinacao;

    @ApiModelProperty(value = "Castração", example = "NAO_INFORMADO")
    private String castracao;

    @ApiModelProperty(value = "Situação atual", example = "PROCURANDO")
    private String situacao_atual;

    @ApiModelProperty(value = "Nome", example = "Bidu")
    private String nome;

    @ApiModelProperty(value = "Cores", example = "MARROM")
    private List<String> cores;

    @ApiModelProperty(value = "ID do usuário publicador")
    private String usuario_id;

    @ApiModelProperty(value = "Nome do usuário publicador")
    private String usuario_nome;

    @ApiModelProperty(value = "Localização")
    private LocalizacaoDTO localizacao;

    @ApiModelProperty(value = "Pet Favorito")
    private boolean petFavoritado = false;
}
