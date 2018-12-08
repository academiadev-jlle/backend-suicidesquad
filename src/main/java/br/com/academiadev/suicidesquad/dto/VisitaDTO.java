package br.com.academiadev.suicidesquad.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VisitaDTO {
    private String usuario_id;
    private String data;
    private PetDTO pet;
}
