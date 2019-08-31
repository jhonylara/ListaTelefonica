package br.edu.aplicacao.entidades;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/** 
 * Entidade Contato: id, nome, email, dt nascimento, usuário (responsável pelo contato) e 
 * 	lista de telefones.
 * 
 * @author vagner.l@uninter.com
 * @author vagnercml@hotmail.com
 *
 */
@Entity
@Table(name="tb_contato")
public class Contato {
	
	@Id
	@Column(name = "id_contato")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "nome", length=150, nullable=false)
	private String nome;
	
	@Column(name = "email", length=70, nullable=true)
	private String email;
	
	@Column(name = "dt_nascimento", nullable=true)
	private Date dtNascimento;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "fk_usuario", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuario;	

	@OneToMany(mappedBy = "contato", fetch=FetchType.LAZY)
	private List<Telefone> telefones;
	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "fk_endereco", referencedColumnName = "id_endereco", nullable = true)
	private Endereco endereco;

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(	
		name="tb_contato_grupo",
		joinColumns=
			@JoinColumn(name="fk_contato", referencedColumnName="id_contato"),
		inverseJoinColumns=
			@JoinColumn(name="fk_grupo", referencedColumnName="id_grupo")
    )
	private List<Grupo> grupos;
	
	@Column(name = "dt_inclusao", nullable=true)
	private Date dtInclusao;
		
	public Contato() {		
	}
	
	public Contato(String nome, String email, Date dtNascimento, Usuario usuario) {
		this.nome = nome;
		this.email = email;
		this.dtNascimento = dtNascimento;
		this.usuario = usuario;
	}
	
	public Contato(String nome, String email, Date dtNascimento) {
		this.nome = nome;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date aniversario) {
		this.dtNascimento = aniversario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
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
		result = prime * result + ((dtNascimento == null) ? 0 : dtNascimento.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		Contato other = (Contato) obj;
		if (dtNascimento == null) {
			if (other.dtNascimento != null)
				return false;
		} else if (!dtNascimento.equals(other.dtNascimento))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
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
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	
}
