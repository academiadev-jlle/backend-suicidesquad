
package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Sexo;

import javax.persistence.AttributeConverter;

public class SexoConverter implements AttributeConverter<Sexo, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Sexo sexo) {
        return sexo.getId();
    }

    @Override
    public Sexo convertToEntityAttribute(Integer dbData) {
        return Sexo.findById(dbData);
    }
}