package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.*;
import br.com.academiadev.suicidesquad.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "pet")
@Data
@NoArgsConstructor
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

    @Builder
    public Pet(Long id, @NotNull Tipo tipo, @NotNull Porte porte, Raca raca, @NotNull ComprimentoPelo comprimentoPelo, SexoPet sexo, @NotNull Categoria categoria, @NotNull Vacinacao vacinacao, @NotNull Castracao castracao, Localizacao localizacao, @Singular("cor") Set<Cor> cores, @Singular List<Registro> registros) {
        this.id = id;
        this.tipo = tipo;
        this.porte = porte;
        this.raca = raca;
        this.comprimentoPelo = comprimentoPelo;
        this.sexo = Optional.ofNullable(sexo).orElse(this.sexo);
        this.categoria = categoria;
        this.vacinacao = Optional.ofNullable(vacinacao).orElse(this.vacinacao);
        this.castracao = Optional.ofNullable(castracao).orElse(this.castracao);
        this.localizacao = localizacao;
        this.cores = Optional.ofNullable(cores).orElse(this.cores);
        this.registros = Optional.ofNullable(registros).orElse(this.registros);
    }

    public void addCor(Cor cor) {
        this.cores.add(cor);
    }

    public void addRegistro(Registro registro) {
        this.registros.add(registro);
        registro.setPet(this);
    }

    public void setRegistros(List<Registro> registros) {
        registros.forEach(this::addRegistro);
    }
}
