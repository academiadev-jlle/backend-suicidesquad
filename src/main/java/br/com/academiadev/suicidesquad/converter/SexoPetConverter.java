package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.SexoPet;

import javax.persistence.AttributeConverter;

public class SexoPetConverter implements AttributeConverter<SexoPet, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SexoPet sexo) {
        return sexo.getId();
    }

    @Override
    public SexoPet convertToEntityAttribute(Integer dbData) {
        return SexoPet.findById(dbData);
    }
}
