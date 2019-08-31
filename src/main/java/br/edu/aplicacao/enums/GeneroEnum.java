package br.edu.aplicacao.enums;

public enum GeneroEnum {
	MASCULINO(1, "Masculino"),
	FEMININO(2, "Feminino");
	
	private final int codigo;
	private final String descricao;
	
	private GeneroEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static GeneroEnum codigoIntParaEnum(int codigoInt) {
		
		GeneroEnum generoEnum = null;
		
		GeneroEnum[] valores = GeneroEnum.values();
		
		for (int i = 0; i < valores.length && generoEnum == null; i++) {
			GeneroEnum categoriaTelefoneEnum = valores[i];
			
			if(categoriaTelefoneEnum.getCodigo() == codigoInt)
				generoEnum = categoriaTelefoneEnum;
		}
		
		return generoEnum;
	}	
}
