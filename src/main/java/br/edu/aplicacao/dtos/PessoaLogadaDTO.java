package br.edu.aplicacao.dtos;

import java.io.Serializable;
import java.util.Date;

public class PessoaLogadaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String nome;	
	private Boolean genero;	
	private Date dtNascimento;
	
	public PessoaLogadaDTO(Long id, String nome, Boolean genero, Date dtNascimento) {
		super();
		this.id = id;
		this.nome = nome;
		this.genero = genero;
		this.dtNascimento = dtNascimento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Boolean getGenero() {
		return genero;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	
}
