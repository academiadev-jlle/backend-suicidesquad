package br.com.academiadev.suicidesquad.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.academiadev.suicidesquad.converter.SexoUsuarioConverter;
import br.com.academiadev.suicidesquad.enums.SexoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "usuario_fotos")
    @Builder.Default
    private List<String> fotos = new ArrayList<>();
    
    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setUsuario(this);
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
    
    public void addFoto(String foto) {
    	this.fotos.add(foto);
    }
    
    public void removeFoto(String foto) {
    	this.fotos.remove(foto);
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
