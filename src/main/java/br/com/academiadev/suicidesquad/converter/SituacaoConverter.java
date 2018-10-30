package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Situacao;

import javax.persistence.AttributeConverter;

public class SituacaoConverter implements AttributeConverter<Situacao, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Situacao situacao) {
        return situacao.getId();
    }

    @Override
    public Situacao convertToEntityAttribute(Integer dbData) {
        return Situacao.findById(dbData);
    }
}
