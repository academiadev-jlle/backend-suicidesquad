package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Porte;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PorteConverter implements AttributeConverter<Porte, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Porte porte) {
        return porte == null ? null : porte.getId();
    }

    @Override
    public Porte convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Porte.findById(dbData);
    }
}
