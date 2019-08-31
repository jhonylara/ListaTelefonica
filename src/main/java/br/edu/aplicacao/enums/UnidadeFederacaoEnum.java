package br.edu.aplicacao.enums;

public enum UnidadeFederacaoEnum {
	AC(1,"AC"),
	AL(2,"AL"),
	AM(3,"AM"),
	AP(4,"AP"),
	BA(5,"BA"),
	CE(6,"CE"),
	DF(7,"DF"),
	ES(8,"ES"),
	GO(9,"GO"),
	MA(10,"MA"),
	MG(11,"MG"),
	MS(12,"MS"),
	MT(13,"MT"),
	PA(14,"PA"),
	PB(15,"PB"),
	PE(16,"PE"),
	PI(17,"PI"),
	PR(18,"PR"),
	RJ(19,"RJ"),
	RN(20,"RN"),
	RO(21,"RO"),
	RR(22,"RR"),
	RS(23,"RS"),
	SC(24,"SC"),
	SE(25,"SE"),
	SP(26,"SP"),
	TO(27,"TO");
	
	private final int codigo;
	private final String descricao;
	
	private UnidadeFederacaoEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static UnidadeFederacaoEnum codigoIntParaEnum(int codigoInt) {
		
		UnidadeFederacaoEnum ufEnum = null;
		
		UnidadeFederacaoEnum[] valores = UnidadeFederacaoEnum.values();
		
		for (int i = 0; i < valores.length && ufEnum == null; i++) {
			UnidadeFederacaoEnum _enum = valores[i];
			
			if(_enum.getCodigo() == codigoInt)
				ufEnum = _enum;
		}
		
		return ufEnum;
	}	
	
}
