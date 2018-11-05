package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.SexoUsuarioConverter;
import br.com.academiadev.suicidesquad.enums.SexoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario extends BaseEntity {
    @NotNull
    @Size(min = 1, max = 120)
    private String nome;

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    @NotNull
    private String senha;

    @NotNull
    @Convert(converter = SexoUsuarioConverter.class)
    private SexoUsuario sexo;

    @NotNull
    @JsonProperty("data_nascimento")
    private LocalDate dataNascimento;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_localizacao")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Localizacao localizacao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Singular
    private List<Telefone> telefones = new ArrayList<>();

    public void addTelefone(Telefone telefone) {
        this.telefones.add(telefone);
        telefone.setUsuario(this);
    }

    private void setTelefones(List<Telefone> telefones) {
        telefones.forEach(this::addTelefone);
    }
}
