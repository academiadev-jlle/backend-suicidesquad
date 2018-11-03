package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.ComprimentoPelo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ComprimentoPeloConverter implements AttributeConverter<ComprimentoPelo, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ComprimentoPelo comprimentoPelo) {
        return comprimentoPelo == null ? null : comprimentoPelo.getId();
    }

    @Override
    public ComprimentoPelo convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : ComprimentoPelo.findById(dbData);
    }
}
