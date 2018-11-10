package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.*;
import br.com.academiadev.suicidesquad.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pet")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pet extends AuditableEntity<Long> {
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
    @Builder.Default
    private SexoPet sexo = SexoPet.NAO_INFORMADO;

    @JsonProperty("categoria")
    @Convert(converter = CategoriaConverter.class)
    @NotNull
    private Categoria categoria;

    @JsonProperty("vacinacao")
    @Convert(converter = VacinacaoConverter.class)
    @NotNull
    @Builder.Default
    private Vacinacao vacinacao = Vacinacao.NAO_INFORMADO;

    @JsonProperty("castracao")
    @Convert(converter = CastracaoConverter.class)
    @NotNull
    @Builder.Default
    private Castracao castracao = Castracao.NAO_INFORMADO;

    @ManyToOne
    @JoinColumn(name = "id_localizacao")
    private Localizacao localizacao;

    @ElementCollection(targetClass = Cor.class)
    @CollectionTable(name = "pet_cor")
    @Convert(converter = CorConverter.class)
    @Singular("cor")
    private Set<Cor> cores = new HashSet<>();

    @OneToMany(mappedBy = "pet", orphanRemoval = true)
    @Singular
    private List<Registro> registros = new ArrayList<>();

    @JsonProperty("nome")
    @Size(min = 2, max = 80)
    private String nome;

    @JsonProperty("descricao")
    @Size(min = 1, max = 255)
    private String descricao;

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
