package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.SexoUsuarioConverter;
import br.com.academiadev.suicidesquad.enums.SexoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario extends AuditableEntity<Long> implements UserDetails {
    @NotNull
    @Size(min = 1, max = 120)
    private String nome;

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    private String senha;

    @NotNull
    @Convert(converter = SexoUsuarioConverter.class)
    @Builder.Default
    private SexoUsuario sexo = SexoUsuario.NAO_INFORMADO;

    private LocalDate dataNascimento;

    private String facebookUserId;

    @ManyToOne(
            targetEntity = Localizacao.class,
            cascade = CascadeType.PERSIST
    )
    private Localizacao localizacao;

    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @Builder.Default
    private List<Telefone> telefones = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "usuario",
            orphanRemoval = true
    )
    private List<Visita> visitas = new ArrayList<>();

    @Builder.Default
    @NotNull
    private boolean emailPublico = true;

    public boolean isEmailPublico(){
        return emailPublico;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setUsuario(this);
    }

    public String getEmailPublico() {
        if (this.emailPublico) {
            return email;
        } else {
            return null;
        }
    }

    public void setPets(List<Pet> pets) {
        pets.forEach(this::addPet);
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void addTelefone(Telefone telefone) {
        this.telefones.add(telefone);
        telefone.setUsuario(this);
    }

    public void setTelefones(List<Telefone> telefones) {
        if (telefones == null || telefones.size() == 0) {
            this.telefones.clear();
        } else {
            telefones.forEach(this::addTelefone);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
