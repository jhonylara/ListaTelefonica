package br.edu.aplicacao.backingbeans.usuario;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class PesquisarUsuarioBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IUsuarioDAO dao = new UsuarioDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// campos para filtro/pesquisa
	private String nome;
	private String login;
	
	// campos usados no resultado da pesquisa
	private List<Usuario> listaDeUsuarios;
	private Integer totalDeUsuariosPesquisa = 0;
	
	public PesquisarUsuarioBB() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public Integer getTotalDeUsuariosPesquisa() {
		return totalDeUsuariosPesquisa;
	}

	public void setTotalDeUsuariosPesquisa(Integer totalDeUsuariosPesquisa) {
		this.totalDeUsuariosPesquisa = totalDeUsuariosPesquisa;
	}

	public List<Usuario> getListaDeUsuarios() {
		return listaDeUsuarios;
	}

	public void setListaDeUsuarios(List<Usuario> listaDeUsuarios) {
		this.listaDeUsuarios = listaDeUsuarios;
	}

	public void preparar() {
		try {
			listaDeUsuarios = dao.listarTodos();
			
			atualizaTotalDeUsuariosPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}

	private void atualizaTotalDeUsuariosPesquisa() {
		if(listaDeUsuarios != null)
			totalDeUsuariosPesquisa = listaDeUsuarios.size();
	}	
	
	public void buscar() {
		try {
			listaDeUsuarios = dao.pesquisar(login, nome);
			
			atualizaTotalDeUsuariosPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}
	
	public String redirecionarParaManterUsuario(Long idUsuarioSelecionado) {
		return "/pages/usuario/manter_usuario.jsf?faces-redirect=true&idUsuarioPesquisa=" + idUsuarioSelecionado;
	}
	
	public void removerUsuario(Long idUsuarioRemovido) {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new UsuarioDAOImpl(em);
				
		try {					
			em.getTransaction().begin();
			
			Usuario usuario = dao.buscarPor(idUsuarioRemovido);
									
			dao.deletar(usuario);
		
			if(em.getTransaction().isActive())
				em.getTransaction().commit();						
			
			MensagensJSFUtils.adicionarMsgInfo("Usuário excluído com sucesso", "");
			
			preparar();
		} catch (Exception e) {
			
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
			
			try {
				if(em.getTransaction().isActive())
					em.getTransaction().rollback();	
			} catch (Exception e2) {
				throw e2;
			}		
			
			throw e;			
		} finally {
			if(em != null && em.isOpen())
				em.close();
		}

		dao = new UsuarioDAOImpl();
	}	
}
