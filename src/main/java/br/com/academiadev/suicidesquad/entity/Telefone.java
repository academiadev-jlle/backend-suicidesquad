package br.com.academiadev.suicidesquad.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "telefone")
@Data
public class Telefone implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    private Usuario usuario;

    @NotBlank
    private String numero;

    @NotNull
    private boolean isWhatsapp;
}
