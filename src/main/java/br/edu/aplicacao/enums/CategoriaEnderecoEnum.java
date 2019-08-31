package br.edu.aplicacao.enums;

public enum CategoriaEnderecoEnum {
	RESIDENCIAL(1, "Residencial"),
	COMERCIAL(2, "Comercial"),
	ENTREGA(3, "Entrega");
	
	private final int codigo;
	private final String descricao;
	
	private CategoriaEnderecoEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static CategoriaEnderecoEnum codigoIntParaEnum(int codigoInt) {
		
		CategoriaEnderecoEnum categoriaEnum = null;
		
		CategoriaEnderecoEnum[] valores = CategoriaEnderecoEnum.values();
		
		for (int i = 0; i < valores.length && categoriaEnum == null; i++) {
			CategoriaEnderecoEnum _enum = valores[i];
			
			if(_enum.getCodigo() == codigoInt)
				categoriaEnum = _enum;
		}
		
		return categoriaEnum;
	}	

}
