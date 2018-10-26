package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.PorteConverter;
import br.com.academiadev.suicidesquad.converter.TipoConverter;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Tipo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pet")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("tipo")
    @Convert(converter = TipoConverter.class)
    @NotNull
    private Tipo tipo;

    @JsonProperty("porte")
    @Convert(converter = PorteConverter.class)
    @NotNull
    private Porte porte;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_raca")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Raca raca;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_cor")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Cor cor;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_comprimento_pelo")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ComprimentoPelo comprimentoPelo;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_localizacao")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Localizacao localizacao;

    @ManyToMany()
    @JoinTable(
            name = "situacao_pet",
            joinColumns = @JoinColumn(name = "id_pet"),
            inverseJoinColumns = @JoinColumn(name = "id_situacao")
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<Situacao> situacoes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> emails  = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telefone> telefones = new ArrayList<>();

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public Raca getRaca() {
        return raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public ComprimentoPelo getComprimentoPelo() {
        return comprimentoPelo;
    }

    public void setComprimentoPelo(ComprimentoPelo comprimentoPelo) {
        this.comprimentoPelo = comprimentoPelo;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public List<Situacao> getSituacoes() {
        return situacoes;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }
}
