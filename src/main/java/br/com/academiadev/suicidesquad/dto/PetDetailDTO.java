package br.com.academiadev.suicidesquad.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PetDetailDTO extends PetDTO {
    @ApiModelProperty(value = "Usuário publicador")
    private UsuarioDTO usuario;

    @ApiModelProperty(value = "Registros de situação (histórico)")
    private List<RegistroDTO> registros;

    @ApiModelProperty(value = "Email para contato", example = "fulano@example.com")
    private String email;

    @ApiModelProperty(value = "Telefones para contato")
    private List<String> telefones;

    @ApiModelProperty(value = "Descrição")
    private String descricao;
}
