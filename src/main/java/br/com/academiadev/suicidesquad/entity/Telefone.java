package br.com.academiadev.suicidesquad.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "telefone")
@Data
@EqualsAndHashCode(callSuper = true, exclude = "usuario")
@NoArgsConstructor
public class Telefone extends BaseEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank
    private String numero;

    @NotNull
    private boolean isWhatsapp;

    public Telefone(@NotBlank String numero) {
        this.numero = numero;
    }
}
