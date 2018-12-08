package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetSearch {
    @NotNull
    private Tipo tipo;

    private SexoPet sexo;

    @Builder.Default
    private Boolean mostrarEntregues = false;

    @Builder.Default
    private Set<Porte> portes = new HashSet<>();

    @Builder.Default
    private Set<Raca> racas = new HashSet<>();

    @Builder.Default
    private Set<ComprimentoPelo> pelos = new HashSet<>();

    @Builder.Default
    private Set<Categoria> categorias = new HashSet<>();

    @Builder.Default
    private Set<Vacinacao> vacinacoes = new HashSet<>();

    @Builder.Default
    private Set<Castracao> castracoes = new HashSet<>();

    @Builder.Default
    private Set<Cor> cores = new HashSet<>();
}
