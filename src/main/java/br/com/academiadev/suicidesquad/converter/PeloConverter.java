package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Pelo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PeloConverter implements AttributeConverter<Pelo, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Pelo comprimentoPelo) {
        return comprimentoPelo == null ? null : comprimentoPelo.getId();
    }

    @Override
    public Pelo convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Pelo.findById(dbData);
    }
}
