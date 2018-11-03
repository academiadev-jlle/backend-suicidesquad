package br.com.academiadev.suicidesquad.enums;

public enum SexoPet {
    MACHO(1),
    FEMEA(2),
    NAO_INFORMADO(3);

    private Integer id;

    SexoPet(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static SexoPet findById(Integer id) {
        for (SexoPet sexo : values()) {
            if (sexo.getId().equals(id)) {
                return sexo;
            }
        }
        throw new IllegalArgumentException("Nenhum SexoPet com o id " + id);
    }
}
