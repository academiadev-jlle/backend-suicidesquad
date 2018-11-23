package br.com.academiadev.suicidesquad.entity;

import br.com.academiadev.suicidesquad.enums.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class PetSearch {
    @NotNull
    private Tipo tipo;

    private Set<SexoPet> sexos = new HashSet<>();

    private Set<Porte> portes = new HashSet<>();

    private Set<Raca> racas = new HashSet<>();

    private Set<ComprimentoPelo> pelos = new HashSet<>();

    private Set<Categoria> categorias = new HashSet<>();

    private Set<Vacinacao> vacinacoes = new HashSet<>();

    private Set<Castracao> castracoes = new HashSet<>();

    private Set<Cor> cores = new HashSet<>();

    public PetSearch(@NotNull Tipo tipo, Set<SexoPet> sexos, Set<Porte> portes, Set<Raca> racas, Set<ComprimentoPelo> pelos, Set<Categoria> categorias, Set<Vacinacao> vacinacoes, Set<Castracao> castracoes, Set<Cor> cores) {
        this.tipo = tipo;

        if (sexos != null) this.sexos = sexos;
        if (portes != null) this.portes = portes;
        if (racas != null) this.racas = racas;
        if (pelos != null) this.pelos = pelos;
        if (categorias != null) this.categorias = categorias;
        if (vacinacoes != null) this.vacinacoes = vacinacoes;
        if (castracoes != null) this.castracoes = castracoes;
        if (cores != null) this.cores = cores;
    }
}
