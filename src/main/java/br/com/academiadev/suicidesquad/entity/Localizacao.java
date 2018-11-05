package br.com.academiadev.suicidesquad.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "localizacao")
@Data
@EqualsAndHashCode(callSuper = true)
public class Localizacao extends BaseEntity<Long> {
    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;
}
