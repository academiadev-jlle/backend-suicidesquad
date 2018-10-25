package br.com.academiadev.suicidesquad.enums;

public enum Tipo {
    CACHORRO(1),
    GATO(2),
    EQUINO(3);

    private final Integer id;

    Tipo(Integer code) {
        this.id = code;
    }

    public Integer getId() {
        return id;
    }

    public static Tipo findById(Integer id) {
        for (Tipo tipo : values()) {
            if (tipo.getId().equals(id)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Nenhum Tipo com o id " + id);
    }
}
