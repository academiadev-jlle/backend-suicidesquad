package br.com.academiadev.suicidesquad.enums;

public enum Castracao {
	NAO_INFORMADO(1),
	NAO_CASTRADO(2),
	CASTRADO(3);
	
	private Integer id;
	
	Castracao(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public static Castracao findById(Integer id) {
		for (Castracao castracao: values())
			if (castracao.getId().equals(id))
				return castracao;
		
		throw new IllegalArgumentException("Nenhuma Castracao com o id " + id);
	}
}
