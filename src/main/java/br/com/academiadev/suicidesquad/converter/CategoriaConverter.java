package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.Categoria;

import javax.persistence.AttributeConverter;

public class CategoriaConverter implements AttributeConverter<Categoria, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Categoria categoria) {
        return categoria.getId();
    }

    @Override
    public Categoria convertToEntityAttribute(Integer dbData) {
        return Categoria.findById(dbData);
    }
}
