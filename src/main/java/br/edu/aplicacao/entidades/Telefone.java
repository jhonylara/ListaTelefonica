package br.edu.aplicacao.entidades;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.edu.aplicacao.enums.CategoriaTelefoneEnum;

/**
 * Entidade Telefone: id, cód. de área, DDD, número, categoria/tipo e contato.
 * 
 * @author vagner.l@uninter.com
 * @author vagnercml@hotmail.com
 *
 */
@Entity
@Table(name="tb_telefone")
public class Telefone {
	
	@Id
	@Column(name = "id_telefone")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "codigo_area", nullable=true)
	private Integer codigoArea;
	
	@Column(name = "ddd", nullable=true)
	private Integer ddd;
	
	@Column(name = "numero", nullable=false)
	private Long numero;
	
	@Enumerated
    @Column(name = "enum_categoria", nullable=false)
	private CategoriaTelefoneEnum categoria;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_contato", referencedColumnName = "id_contato", nullable = false)	
	private Contato contato;
	
	public Telefone() {
	}

	public Telefone(Integer codigoArea, Integer ddd, Long numero, CategoriaTelefoneEnum categoria) {
		this.codigoArea = codigoArea;
		this.ddd = ddd;
		this.numero = numero;
		this.categoria = categoria;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public CategoriaTelefoneEnum getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaTelefoneEnum categoria) {
		this.categoria = categoria;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((codigoArea == null) ? 0 : codigoArea.hashCode());
		result = prime * result + ((contato == null) ? 0 : contato.hashCode());
		result = prime * result + ((ddd == null) ? 0 : ddd.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
		Telefone other = (Telefone) obj;
		if (categoria != other.categoria)
			return false;
		if (codigoArea == null) {
			if (other.codigoArea != null)
				return false;
		} else if (!codigoArea.equals(other.codigoArea))
			return false;
		if (contato == null) {
			if (other.contato != null)
				return false;
		} else if (!contato.equals(other.contato))
			return false;
		if (ddd == null) {
			if (other.ddd != null)
				return false;
		} else if (!ddd.equals(other.ddd))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	
}
