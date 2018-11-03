package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Tipo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TipoConverter implements AttributeConverter<Tipo, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Tipo tipo) {
        return tipo == null ? null : tipo.getId();
    }

    @Override
    public Tipo convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Tipo.findById(dbData);
    }
}
