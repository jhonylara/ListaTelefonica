package br.edu.aplicacao.enums;

public enum CategoriaTelefoneEnum {
	RESIDENCIAL(1, "Residencial"),
	COMERCIAL(2, "Comercial"),
	CELULAR(3, "Celular");
	
	private final int codigo;
	private final String descricao;
	
	private CategoriaTelefoneEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static CategoriaTelefoneEnum codigoIntParaEnum(int codigoInt) {
		
		CategoriaTelefoneEnum categoriaEnum = null;
		
		CategoriaTelefoneEnum[] valores = CategoriaTelefoneEnum.values();
		
		for (int i = 0; i < valores.length && categoriaEnum == null; i++) {
			CategoriaTelefoneEnum categoriaTelefoneEnum = valores[i];
			
			if(categoriaTelefoneEnum.getCodigo() == codigoInt)
				categoriaEnum = categoriaTelefoneEnum;
		}
		
		return categoriaEnum;
	}	
}
