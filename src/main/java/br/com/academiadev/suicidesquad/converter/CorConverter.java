package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Cor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CorConverter implements AttributeConverter<Cor, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Cor cor) {
        return cor == null ? null : cor.getId();
    }

    @Override
    public Cor convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Cor.findById(dbData);
    }
}
