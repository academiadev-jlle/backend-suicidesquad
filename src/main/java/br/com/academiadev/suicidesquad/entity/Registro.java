package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.SituacaoConverter;
import br.com.academiadev.suicidesquad.enums.Situacao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Registro extends BaseEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "id_pet")
    @NotNull
    private Pet pet;

    @Convert(converter = SituacaoConverter.class)
    @NotNull
    private Situacao situacao;

    private LocalDateTime data;

    public Registro(@NotNull Pet pet, @NotNull Situacao situacao) {
        this.pet = pet;
        this.situacao = situacao;
        this.data = LocalDateTime.now();
    }

    public Registro(@NotNull Situacao situacao) {
        this.situacao = situacao;
    }
}
