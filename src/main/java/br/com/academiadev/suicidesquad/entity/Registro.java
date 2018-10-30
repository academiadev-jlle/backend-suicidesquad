package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.SituacaoConverter;
import br.com.academiadev.suicidesquad.enums.Situacao;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro")
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

    public Registro() {
    }

    public Registro(@NotNull Pet pet, @NotNull Situacao situacao) {
        this.pet = pet;
        this.situacao = situacao;
        this.data = LocalDateTime.now();
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
