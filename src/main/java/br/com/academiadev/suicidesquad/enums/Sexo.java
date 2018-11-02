package br.com.academiadev.suicidesquad.enums;

public enum Sexo {
    MACHO(1),
    FEMEA(2),
    MASCULINO(3),
    FEMININO(4);
  
    private Integer id;

    Sexo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Sexo findById(Integer id) {
        for (Sexo sexo: values()) {
            if (sexo.getId().equals(id)) {
                return sexo;
            }
        }
        throw new IllegalArgumentException("Nenhum Sexo com o id " + id);
    }
}
