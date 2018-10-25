package br.com.academiadev.suicidesquad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
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

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "id_tipo", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Tipo tipo;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "id_raca", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Raca raca;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "id_porte", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Porte porte;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "id_cor", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Cor cor;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "id_comprimento_pelo", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ComprimentoPelo comprimentoPelo;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "id_localizacao", nullable = false)
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

    public Raca getRaca() {
        return raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
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
