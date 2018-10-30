package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Raca;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RacaConverter implements AttributeConverter<Raca, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Raca raca) {
        if (raca == null ) {
            return null;
        }
        return raca.getId();
    }

    @Override
    public Raca convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Raca.findById(dbData);
    }
}
