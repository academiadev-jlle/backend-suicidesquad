package br.com.academiadev.suicidesquad.enums;

public enum Situacao {
    PROCURANDO(1),
    ENCONTRADO(2),
    ENTREGUE(3);

    private Integer id;

    Situacao(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Situacao findById(Integer id) {
        for (Situacao situacao: values()) {
            if (situacao.getId().equals(id)) {
                return situacao;
            }
        }
        throw new IllegalArgumentException("Nenhuma Situacao com o id " + id);
    }
}
