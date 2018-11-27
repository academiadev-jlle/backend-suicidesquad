package br.com.academiadev.suicidesquad.dto;

import br.com.academiadev.suicidesquad.enums.Situacao;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RegistroCreateDTO {
    @ApiModelProperty(value = "Situação", allowableValues = "PROCURANDO,ENCONTRADO,ENTREGUE")
    @NotNull
    private Situacao situacao;
}
