package br.com.academiadev.suicidesquad.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "telefone")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Telefone extends BaseEntity<Long> {
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Usuario usuario;

    @NotBlank
    private String numero;

    @NotNull
    private boolean isWhatsapp;

    public Telefone(@NotBlank String numero) {
        this.numero = numero;
    }
}
