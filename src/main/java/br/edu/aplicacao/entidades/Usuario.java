package br.edu.aplicacao.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_usuario")
public class Usuario {

	@Id
	@Column(name = "id_usuario")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
		
	@Column(name = "login", length=25, nullable=false)
	private String login;
	
	@Column(name = "senha", length=15, nullable=false)
	private String senha;
	
	@Column(name = "nome", length=150, nullable=false)
	private String nome;
	
	@Column(name = "eh_administrador", nullable=false)
	private Boolean ehAdministrador;
	
	@Column(name = "dt_inclusao", nullable=true)
	private Date dtInclusao;
	
	public Usuario() {		
	}
	
	public Usuario(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}
	
	public Usuario(String login, String senha, String nome, Boolean ehAdm) {
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.ehAdministrador = ehAdm;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getEhAdministrador() {
		return ehAdministrador;
	}
	public void setEhAdministrador(Boolean ehAdministrador) {
		this.ehAdministrador = ehAdministrador;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}
	
	
}
