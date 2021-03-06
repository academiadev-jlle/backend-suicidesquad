package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.converter.*;
import br.com.academiadev.suicidesquad.enums.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pet")
public class Pet extends AuditableEntity<Long> {
    @Convert(converter = TipoConverter.class)
    @NotNull
    private Tipo tipo;

    @Convert(converter = PorteConverter.class)
    @NotNull
    private Porte porte;

    @Convert(converter = RacaConverter.class)
    private Raca raca;

    @Convert(converter = ComprimentoPeloConverter.class)
    @NotNull
    private ComprimentoPelo comprimentoPelo;

    @Convert(converter = SexoPetConverter.class)
    @Builder.Default
    private SexoPet sexo = SexoPet.NAO_INFORMADO;

    @Convert(converter = CategoriaConverter.class)
    @NotNull
    private Categoria categoria;

    @Convert(converter = VacinacaoConverter.class)
    @NotNull
    @Builder.Default
    private Vacinacao vacinacao = Vacinacao.NAO_INFORMADO;

    @Convert(converter = CastracaoConverter.class)
    @NotNull
    @Builder.Default
    private Castracao castracao = Castracao.NAO_INFORMADO;

    @ManyToOne
    @JoinColumn(name = "id_localizacao")
    private Localizacao localizacao;

    @Builder.Default
    @OneToMany(mappedBy = "pet")
    private List<PetFavorito> petFavoritos = new ArrayList<>();

    @ElementCollection(targetClass = Cor.class)
    @CollectionTable(name = "pet_cor")
    @Convert(converter = CorConverter.class)
    @Builder.Default
    private Set<Cor> cores = new HashSet<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Registro> registros = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Visita> visitas = new ArrayList<>();

    @Size(min = 2, max = 80)
    private String nome;

    @Size(min = 1, max = 2000)
    private String descricao;

    private LocalDateTime dataNotificacaoDeInatividade;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public void addCor(Cor cor) {
        this.cores.add(cor);
    }

    public void addPetFavorito(PetFavorito petFavorito){
        this.petFavoritos.add(petFavorito);
    }

    public void addRegistro(Registro registro) {
        this.registros.add(registro);
        registro.setPet(this);
    }

    public void setRegistros(List<Registro> registros) {
        registros.forEach(this::addRegistro);
    }

    public Situacao getSituacaoAtual() {
        return this.registros.stream()
                .max(Comparator.comparing(Registro::getData))
                .map(Registro::getSituacao)
                .orElse(null);
    }

    public void addVisita(Usuario usuario) {
        Visita visita = new Visita(this, usuario);
        this.visitas.add(visita);
        usuario.getVisitas().add(visita);
    }

    public int getNumeroDeVisitas() {
        return this.visitas.size();
    }
}
