package br.com.academiadev.suicidesquad.enums;

public enum Categoria {
    ACHADO(1),
    PERDIDO(2),
    PARA_ADOCAO(3);

    private Integer id;

    Categoria(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Categoria findById(Integer id) {
        for (Categoria categoria : values()) {
            if (categoria.getId().equals(id)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma Categoria com o id " + id);
    }
}
