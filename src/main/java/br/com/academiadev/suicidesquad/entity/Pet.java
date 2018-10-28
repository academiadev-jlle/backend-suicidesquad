package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.CorConverter;
import br.com.academiadev.suicidesquad.converter.PorteConverter;
import br.com.academiadev.suicidesquad.converter.RacaConverter;
import br.com.academiadev.suicidesquad.converter.TipoConverter;
import br.com.academiadev.suicidesquad.enums.Cor;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Raca;
import br.com.academiadev.suicidesquad.enums.Tipo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @JsonProperty("raca")
    @Convert(converter = RacaConverter.class)
    private Raca raca;

    @ManyToOne
    @JoinColumn(name = "id_localizacao")
    private Localizacao localizacao;

    @ElementCollection(targetClass = Cor.class)
    @CollectionTable(name = "pet_cor")
    @Convert(converter = CorConverter.class)
    private Set<Cor> cores = new HashSet<>();

    public Pet() {
    }

    public Pet(@NotNull Tipo tipo, @NotNull Porte porte) {
        this.tipo = tipo;
        this.porte = porte;
    }

    public Pet(@NotNull Tipo tipo, @NotNull Porte porte, @NotNull Raca raca) {
        this.tipo = tipo;
        this.porte = porte;
        this.raca = raca;
    }

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

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Set<Cor> getCores() {
        return cores;
    }

    public void setCores(Set<Cor> cores) {
        this.cores = cores;
    }

    public boolean addCor(Cor cor)  {
        return this.cores.add(cor);
    }

    public boolean removeCor(Cor cor) {
        return this.cores.remove(cor);
    }
}
