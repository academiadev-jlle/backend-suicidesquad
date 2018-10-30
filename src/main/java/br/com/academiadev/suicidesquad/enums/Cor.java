package br.com.academiadev.suicidesquad.enums;

public enum Cor {
    MARROM(1),
    BRANCO(2),
    PRETO(3);

    private Integer id;

    Cor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Cor findById(Integer id){
        for(Cor cor: values()) {
            if (cor.getId().equals(id)) {
                return cor;
            }
        }
        throw new IllegalArgumentException("Nenhuma cor com o id " + id);
    }
}
