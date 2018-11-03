package br.com.academiadev.suicidesquad.enums;

public enum ComprimentoPelo {
    SEM_PELO(1),
    CURTO(2),
    MEDIO(3),
    LONGO(4);

    private Integer id;

    ComprimentoPelo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static ComprimentoPelo findById(Integer id) {
        for (ComprimentoPelo comprimentoPelo : values()) {
            if (comprimentoPelo.getId().equals(id)) {
                return comprimentoPelo;
            }
        }
        throw new IllegalArgumentException("Nenhum ComprimentoPelo com o id " + id);
    }
}
