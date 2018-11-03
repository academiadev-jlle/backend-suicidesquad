package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.*;
import br.com.academiadev.suicidesquad.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @JsonProperty("comprimento_pelo")
    @Convert(converter = ComprimentoPeloConverter.class)
    @NotNull
    private ComprimentoPelo comprimentoPelo;

    @JsonProperty("sexo")
    @Convert(converter = SexoPetConverter.class)
    private SexoPet sexo = SexoPet.NAO_INFORMADO;

    @JsonProperty("categoria")
    @Convert(converter = CategoriaConverter.class)
    @NotNull
    private Categoria categoria;

    @JsonProperty("vacinacao")
    @Convert(converter = VacinacaoConverter.class)
    @NotNull
    private Vacinacao vacinacao = Vacinacao.NAO_INFORMADO;

    @JsonProperty("castracao")
    @Convert(converter = CastracaoConverter.class)
    @NotNull
    private Castracao castracao = Castracao.NAO_INFORMADO;

    @ManyToOne
    @JoinColumn(name = "id_localizacao")
    private Localizacao localizacao;

    @ElementCollection(targetClass = Cor.class)
    @CollectionTable(name = "pet_cor")
    @Convert(converter = CorConverter.class)
    private Set<Cor> cores = new HashSet<>();

    @OneToMany(mappedBy = "pet", orphanRemoval = true)
    private List<Registro> registros = new ArrayList<>();

    public Pet() {
    }

    public Pet(Categoria categoria, @NotNull Tipo tipo, @NotNull Porte porte) {
        this.categoria = categoria;
        this.tipo = tipo;
        this.porte = porte;
    }

    public Pet(Categoria categoria, @NotNull Tipo tipo, @NotNull Porte porte, @NotNull Raca raca) {
        this.categoria = categoria;
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

    public ComprimentoPelo getComprimentoPelo() {
        return comprimentoPelo;
    }

    public void setComprimentoPelo(ComprimentoPelo comprimentoPelo) {
        this.comprimentoPelo = comprimentoPelo;
    }

    public SexoPet getSexo() {
        return sexo;
    }

    public void setSexo(SexoPet sexo) {
        this.sexo = sexo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Vacinacao getVacinacao() {
        return vacinacao;
    }

    public void setVacinacao(Vacinacao vacinacao) {
        this.vacinacao = vacinacao;
    }

    public Castracao getCastracao() {
        return castracao;
    }

    public void setCastracao(Castracao castracao) {
        this.castracao = castracao;
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

    public boolean addCor(Cor cor) {
        return this.cores.add(cor);
    }

    public boolean removeCor(Cor cor) {
        return this.cores.remove(cor);
    }

    public List<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
        registros.forEach(registro -> registro.setPet(this));
    }

    public void addRegistro(Registro registro) {
        this.registros.add(registro);
        registro.setPet(this);
    }
}
