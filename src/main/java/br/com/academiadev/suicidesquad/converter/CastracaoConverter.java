package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Castracao;

import javax.persistence.AttributeConverter;

public class CastracaoConverter implements AttributeConverter<Castracao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Castracao castracao) {
        return castracao.getId();
    }

    @Override
    public Castracao convertToEntityAttribute(Integer id) {
        return Castracao.findById(id);
    }
}
