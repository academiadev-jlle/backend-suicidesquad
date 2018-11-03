package br.com.academiadev.suicidesquad.enums;

public enum SexoUsuario {
    MASCULINO(1),
    FEMININO(2),
    NAO_INFORMADO(3);

    private Integer id;

    SexoUsuario(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static SexoUsuario findById(Integer id) {
        for (SexoUsuario sexo : values()) {
            if (sexo.getId().equals(id)) {
                return sexo;
            }
        }
        throw new IllegalArgumentException("Nenhum SexoUsuario com o id " + id);
    }
}
