package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Raca;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RacaConverter implements AttributeConverter<Raca, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Raca raca) {
        return raca == null ? null : raca.getId();
    }

    @Override
    public Raca convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Raca.findById(dbData);
    }
}
