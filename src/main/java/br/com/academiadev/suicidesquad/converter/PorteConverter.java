package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Tipo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PorteConverter implements AttributeConverter<Porte, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Porte porte) {
        return porte.getId();
    }

    @Override
    public Porte convertToEntityAttribute(Integer dbData) {
        return Porte.findById(dbData);
    }
}
