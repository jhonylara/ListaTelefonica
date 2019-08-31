package br.edu.aplicacao.dtos;

public class ItemListaTelefoneDTO {

	private Boolean ehInserir = false;
	private Boolean ehAlterar = false;

	private Integer sequencia;
	private Long idTelefone;
	private Integer codigoArea;
	private Integer ddd;
	private Long numero;
	private Integer categoriaCodigo;
	private String categoriaDescricao;
	
	public ItemListaTelefoneDTO(Integer sequencia, Long idTelefone, Integer codigoArea, Integer ddd, Long numero,
			Integer categoriaCodigo, String categoriaDescricao) {
		this.sequencia = sequencia;
		this.idTelefone = idTelefone;
		this.codigoArea = codigoArea;
		this.ddd = ddd;
		this.numero = numero;
		this.categoriaCodigo = categoriaCodigo;
		this.categoriaDescricao = categoriaDescricao;
	}
	
	public Boolean getEhInserir() {
		return ehInserir;
	}
	public void setEhInserir(Boolean ehInserir) {
		this.ehInserir = ehInserir;
	}
	public Boolean getEhAlterar() {
		return ehAlterar;
	}
	public void setEhAlterar(Boolean ehAlterar) {
		this.ehAlterar = ehAlterar;
	}
	public Integer getSequencia() {
		return sequencia;
	}
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}
	public Integer getCodigoArea() {
		return codigoArea;
	}
	public void setCodigoArea(Integer codigoArea) {
		this.codigoArea = codigoArea;
	}
	public Integer getDdd() {
		return ddd;
	}
	public void setDdd(Integer ddd) {
		this.ddd = ddd;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getCategoriaDescricao() {
		return categoriaDescricao;
	}
	public void setCategoriaDescricao(String categoriaDescricao) {
		this.categoriaDescricao = categoriaDescricao;
	}

	public Integer getCategoriaCodigo() {
		return categoriaCodigo;
	}

	public void setCategoriaCodigo(Integer categoriaCodigo) {
		this.categoriaCodigo = categoriaCodigo;
	}

	public Long getIdTelefone() {
		return idTelefone;
	}

	public void setIdTelefone(Long idTelefone) {
		this.idTelefone = idTelefone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoriaCodigo == null) ? 0 : categoriaCodigo.hashCode());
		result = prime * result + ((idTelefone == null) ? 0 : idTelefone.hashCode());
		result = prime * result + ((sequencia == null) ? 0 : sequencia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemListaTelefoneDTO other = (ItemListaTelefoneDTO) obj;
		if (categoriaCodigo == null) {
			if (other.categoriaCodigo != null)
				return false;
		} else if (!categoriaCodigo.equals(other.categoriaCodigo))
			return false;
		if (idTelefone == null) {
			if (other.idTelefone != null)
				return false;
		} else if (!idTelefone.equals(other.idTelefone))
			return false;
		if (sequencia == null) {
			if (other.sequencia != null)
				return false;
		} else if (!sequencia.equals(other.sequencia))
			return false;
		return true;
	}
	
	
}
