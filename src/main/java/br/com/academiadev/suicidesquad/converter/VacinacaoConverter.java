package br.com.academiadev.suicidesquad.converter;

import javax.persistence.AttributeConverter;

import br.com.academiadev.suicidesquad.enums.Vacinacao;

public class VacinacaoConverter implements AttributeConverter<Vacinacao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Vacinacao vacinacao) {
        return vacinacao.getId();
    }

    @Override
    public Vacinacao convertToEntityAttribute(Integer dbData) {
        return Vacinacao.findById(dbData);
    }
}
