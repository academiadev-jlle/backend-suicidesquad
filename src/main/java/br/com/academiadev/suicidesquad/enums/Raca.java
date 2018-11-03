package br.com.academiadev.suicidesquad.enums;

public enum Raca {
    CACHORRO_SRD(1, Tipo.CACHORRO),
    GATO_SRD(2, Tipo.GATO),
    EQUINO_SRD(3, Tipo.EQUINO),

    LABRADOR(4, Tipo.CACHORRO),
    PITBULL(5, Tipo.CACHORRO),

    SIAMES(6, Tipo.GATO),
    PERSA(7, Tipo.GATO),

    PURO_SANGUE(8, Tipo.EQUINO),
    LUSITANO(9, Tipo.EQUINO);

    private Integer id;

    private Tipo tipo;

    Raca(Integer id, Tipo tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public static Raca findById(Integer id) {
        for (Raca raca : values()) {
            if (raca.getId().equals(id)) {
                return raca;
            }
        }
        throw new IllegalArgumentException("Nenhuma Raca com o id " + id);
    }
}
