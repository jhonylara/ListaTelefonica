package br.edu.aplicacao.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.edu.aplicacao.enums.CategoriaEnderecoEnum;
import br.edu.aplicacao.enums.TipoEnderecoEnum;
import br.edu.aplicacao.enums.UnidadeFederacaoEnum;

/** 
 * Entidade Endereço: categoria do endereço (residencial, comercial, entrega), tipo (rua, av.,...),
 * 		logradouro, localidade (cidade), número (ou loteamento), complemento, cep, uf
 *  
 * @author vagner.l@uninter.com
 * @author vagnercml@hotmail.com
 *
 */
@Entity
@Table(name="tb_endereco")
public class Endereco {
	
	@Id
	@Column(name = "id_endereco")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "logradouro", length=255, nullable=true)
	private String logradouro; 

	@Column(name = "numero_ou_loteamento", length=70, nullable=true)
	private String numeroOuLoteamento;

	@Enumerated
    @Column(name = "enum_tipo", nullable=true)
	private TipoEnderecoEnum tipo; 
	
	@Column(name = "localidade", length=150, nullable=true)
	private String localidade;	
	
	@Column(name = "complemento", length=150, nullable=true)
	private String complemento;
	
	@Enumerated
    @Column(name = "enum_categoria", nullable=true)
	private CategoriaEnderecoEnum categoria;
	
	@Enumerated
    @Column(name = "enum_uf", nullable=true)
	private UnidadeFederacaoEnum uf;
	
	@Column(name = "cep",  length=15, nullable=true)
	private String cep;
	
	public Endereco() {		
	}

	public Endereco(
			CategoriaEnderecoEnum categoria,
			TipoEnderecoEnum tipo,
			String localidade,
			String logradouro,
			String numeroOuLoteamento, 
			String complemento,
			UnidadeFederacaoEnum uf,
			String cep) {
		
		this.logradouro = logradouro;
		this.localidade = localidade;
		this.numeroOuLoteamento = numeroOuLoteamento;
		this.complemento = complemento;
		this.categoria = categoria;
		this.tipo = tipo;
		this.uf = uf;
		this.cep = cep;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumeroOuLoteamento() {
		return numeroOuLoteamento;
	}

	public void setNumeroOuLoteamento(String numeroOuLoteamento) {
		this.numeroOuLoteamento = numeroOuLoteamento;
	}

	public TipoEnderecoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoEnderecoEnum tipo) {
		this.tipo = tipo;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public CategoriaEnderecoEnum getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEnderecoEnum categoria) {
		this.categoria = categoria;
	}

	public UnidadeFederacaoEnum getUf() {
		return uf;
	}

	public void setUf(UnidadeFederacaoEnum uf) {
		this.uf = uf;
	}
	
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result + ((complemento == null) ? 0 : complemento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((localidade == null) ? 0 : localidade.hashCode());
		result = prime * result + ((logradouro == null) ? 0 : logradouro.hashCode());
		result = prime * result + ((numeroOuLoteamento == null) ? 0 : numeroOuLoteamento.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((uf == null) ? 0 : uf.hashCode());
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
		Endereco other = (Endereco) obj;
		if (categoria != other.categoria)
			return false;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (complemento == null) {
			if (other.complemento != null)
				return false;
		} else if (!complemento.equals(other.complemento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (localidade == null) {
			if (other.localidade != null)
				return false;
		} else if (!localidade.equals(other.localidade))
			return false;
		if (logradouro == null) {
			if (other.logradouro != null)
				return false;
		} else if (!logradouro.equals(other.logradouro))
			return false;
		if (numeroOuLoteamento == null) {
			if (other.numeroOuLoteamento != null)
				return false;
		} else if (!numeroOuLoteamento.equals(other.numeroOuLoteamento))
			return false;
		if (tipo != other.tipo)
			return false;
		if (uf != other.uf)
			return false;
		return true;
	}

	
}
