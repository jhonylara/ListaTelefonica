package br.edu.aplicacao.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_pessoa")
public class Pessoa {
	
	@Id
	@Column(name = "id_pessoa")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
		
	@Column(name = "nome", length=25, nullable=false)
	private String nome;
	
	@Column(name = "genero", nullable=false)
	private Boolean genero;
	
	@Column(name = "dt_nascimento", nullable=true)
	private Date dtNascimento;
	
	public Pessoa() {		
	}
	
	public Pessoa(String nome, Boolean genero, Date dtNascimento) {
		this.nome = nome;
		this.genero = genero;
		this.dtNascimento = dtNascimento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getGenero() {
		return genero;
	}

	public void setGenero(Boolean genero) {
		this.genero = genero;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	
	
	

}
