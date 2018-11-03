package br.com.academiadev.suicidesquad.enums;

public enum Vacinacao {
    NAO_INFORMADO(1),
    PARCIAL(2),
    EM_DIA(3);

    private Integer id;

    Vacinacao(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public static Vacinacao findById(Integer id) {
        for (Vacinacao vacinacao : values())
            if (vacinacao.getId().equals(id))
                return vacinacao;

        throw new IllegalArgumentException("Nenhuma Vacinacao com o id " + id);
    }
}
