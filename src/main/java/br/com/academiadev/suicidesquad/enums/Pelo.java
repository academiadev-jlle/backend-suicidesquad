package br.com.academiadev.suicidesquad.enums;

public enum Pelo {
    SEM_PELO(1),
    CURTO(2),
    MEDIO(3),
    LONGO(4);

    private Integer id;

    Pelo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Pelo findById(Integer id) {
        for (Pelo comprimentoPelo : values()) {
            if (comprimentoPelo.getId().equals(id)) {
                return comprimentoPelo;
            }
        }
        throw new IllegalArgumentException("Nenhum ComprimentoPelo com o id " + id);
    }
}
