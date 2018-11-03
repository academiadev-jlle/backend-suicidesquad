package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.SexoUsuario;

import javax.persistence.AttributeConverter;

public class SexoUsuarioConverter implements AttributeConverter<SexoUsuario, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SexoUsuario sexo) {
        return sexo.getId();
    }

    @Override
    public SexoUsuario convertToEntityAttribute(Integer dbData) {
        return SexoUsuario.findById(dbData);
    }
}
