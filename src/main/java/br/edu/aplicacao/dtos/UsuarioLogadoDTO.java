package br.edu.aplicacao.dtos;

import java.io.Serializable;
import java.util.Date;

import br.edu.aplicacao.entidades.Usuario;

public class UsuarioLogadoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String login;	
	private String nome;	
	private Boolean ehAdministrador;
	private Date dataHoraLogin;
	
	public UsuarioLogadoDTO(Long id, String login, String nome, Boolean ehAdministrador, Date dataHoraLogin) {
		super();
		this.id = id;
		this.login = login;
		this.nome = nome;
		this.ehAdministrador = ehAdministrador;
		this.dataHoraLogin = dataHoraLogin;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getNome() {
		return nome;
	}

	public Boolean getEhAdministrador() {
		return ehAdministrador;
	}

	public Date getDataHoraLogin() {
		return dataHoraLogin;
	}

	
}
