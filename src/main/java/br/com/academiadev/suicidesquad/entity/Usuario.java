package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.SexoConverter;
import br.com.academiadev.suicidesquad.enums.Sexo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Size(min = 1, max = 120)
    private String nome;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String senha;


    public Usuario() {

    }

    @NotNull
    @Convert(converter = SexoConverter.class)
    private Sexo sexo;

    @NotNull
    private LocalDate dataNascimento;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_localizacao")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Localizacao localizacao;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_telefone")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Telefone telefone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Usuario(@NotNull String nome, @NotNull String email, @NotNull String senha, @NotNull Sexo sexo, @NotNull LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
    }
}
