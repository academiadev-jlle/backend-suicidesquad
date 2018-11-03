package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Vacinacao;

import javax.persistence.AttributeConverter;

public class VacinacaoConverter implements AttributeConverter<Vacinacao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Vacinacao vacinacao) {
        return vacinacao == null ? null : vacinacao.getId();
    }

    @Override
    public Vacinacao convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Vacinacao.findById(dbData);
    }
}
