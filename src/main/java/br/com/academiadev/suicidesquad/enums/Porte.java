package br.com.academiadev.suicidesquad.enums;

public enum Porte {
    PEQUENO(1),
    MEDIO(2),
    GRANDE(3);

    private Integer id;

    Porte(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Porte findById(Integer id) {
        for (Porte porte: values()) {
            if (porte.getId().equals(id)) {
                return porte;
            }
        }
        throw new IllegalArgumentException("Nenhum Porte com o id " + id);
    }
}
