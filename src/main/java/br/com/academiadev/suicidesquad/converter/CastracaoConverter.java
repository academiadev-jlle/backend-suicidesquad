package br.com.academiadev.suicidesquad.converter;

import javax.persistence.AttributeConverter;

import br.com.academiadev.suicidesquad.enums.Castracao;

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
