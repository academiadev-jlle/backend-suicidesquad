package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.SituacaoConverter;
import br.com.academiadev.suicidesquad.enums.Situacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro")
@Data
@NoArgsConstructor
public class Registro implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pet")
    @NotNull
    @JsonIgnore
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
