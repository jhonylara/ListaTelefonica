package br.edu.aplicacao.backingbeans.grupo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Grupo;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.exceptions.CamposObrigatoriosNaoInformadosException;
import br.edu.aplicacao.exceptions.GrupoNomeJahCadastradoException;
import br.edu.aplicacao.persistencia.interfaces.IGrupoDAO;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.GrupoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.java.utils.StringsUtils;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class ManterGrupoBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IGrupoDAO dao = new GrupoDAOImpl();
	
	private IUsuarioDAO daoUsario = new UsuarioDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Long id;	
	private String nome;	

	private Boolean ehAlteracao = false;
	
	public ManterGrupoBB() {
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
	public Boolean getEhAlteracao() {
		return ehAlteracao;
	}

	public void setEhAlteracao(Boolean ehAlteracao) {
		this.ehAlteracao = ehAlteracao;
	}

	public void incluir() {

		try {
			validarCamposObrigatorios();
			
			validarGrupoJahCadastradoInclusao();
			
			incluirObjeto();
		
			MensagensJSFUtils.adicionarMsgInfo("Grupo incluído com sucesso", "");
		} catch (
				CamposObrigatoriosNaoInformadosException | 
				GrupoNomeJahCadastradoException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");		
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}
	}

	public void salvarAlteracao() {
		
		try {
			validarCamposObrigatorios();
			
			alterarObjeto();
			
			MensagensJSFUtils.adicionarMsgInfo("Grupo alterado com sucesso", "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}
	}

	private void alterarObjeto() {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new GrupoDAOImpl(em);

		try {
			em.getTransaction().begin();
			
			Grupo grupo = dao.buscarPor(id);
			
			grupo.setNome(nome);
			
			dao.alterar(grupo);
			
			if(em.getTransaction().isActive())
				em.getTransaction().commit();
			
		} catch (Exception e) {
			
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
		
		dao = new GrupoDAOImpl();
	}
	
	public void prepararAlteracao(Long idGrupoPesquisa) {
		if(idGrupoPesquisa != null) {
						
			Grupo grupo = dao.buscarPor(idGrupoPesquisa);
			
			if(grupo == null) {
				String msgErro = "Código/Id de grupo inválido!"; 
				MensagensJSFUtils.adicionarMsgErro(msgErro, "");
			
				logger.error(msgErro);
				
				return;
			}
			
			this.id = idGrupoPesquisa;
			this.nome = grupo.getNome();
			this.ehAlteracao = true;
		}
	}

	private void validarGrupoJahCadastradoInclusao() throws GrupoNomeJahCadastradoException {
		
		UsuarioLogadoDTO usuarioLogado = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
		
		List<Grupo> resultadoPesquisa = dao.pesquisar(null, usuarioLogado.getId());
		
		if(resultadoPesquisa != null) {
			for (Iterator<Grupo> iterator = resultadoPesquisa.iterator(); iterator.hasNext();) {
				Grupo grupo = (Grupo) iterator.next();
				
				if(grupo.getNome().compareTo(nome) == 0)
					throw new GrupoNomeJahCadastradoException();
			}
		}
	}
	
	private void incluirObjeto() {
		dao = null;
					
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new GrupoDAOImpl(em);
				
		try {		
			em.getTransaction().begin();	
			
			UsuarioLogadoDTO usuarioLogado = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
			
			Usuario usuario = daoUsario.buscarPor(usuarioLogado.getId());
			
			Grupo grupo = new Grupo(nome, usuario);
			
			dao.inserir(grupo);	
			
			if(em.getTransaction().isActive())
				em.getTransaction().commit();						
		} catch (Exception e) {
			
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
		
		dao = new GrupoDAOImpl();
	}

	private void validarCamposObrigatorios() throws CamposObrigatoriosNaoInformadosException {
		if(StringsUtils.ehStringVazia(nome))
			throw new CamposObrigatoriosNaoInformadosException("nome");
	}
}
