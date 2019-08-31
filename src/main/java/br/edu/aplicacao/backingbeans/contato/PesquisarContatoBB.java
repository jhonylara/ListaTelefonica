package br.edu.aplicacao.backingbeans.contato;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.persistencia.interfaces.IContatoDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.ContatoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class PesquisarContatoBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IContatoDAO dao = new ContatoDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// campos para filtro/pesquisa
	private String nome;
	
	// campos usados no resultado da pesquisa
	private List<Contato> listaDeContatos;
	private Integer totalDeContatosPesquisa = 0;
	
	public PesquisarContatoBB() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Contato> getListaDeContatos() {
		return listaDeContatos;
	}

	public void setListaDeContatos(List<Contato> listaDeContatos) {
		this.listaDeContatos = listaDeContatos;
	}

	public Integer getTotalDeContatosPesquisa() {
		return totalDeContatosPesquisa;
	}

	public void setTotalDeContatosPesquisa(Integer totalDeContatosPesquisa) {
		this.totalDeContatosPesquisa = totalDeContatosPesquisa;
	}
	
	public void preparar() {	
		try {
			UsuarioLogadoDTO usuarioLogadoDto = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
			
			listaDeContatos = dao.listarContatosPorUsuario( usuarioLogadoDto.getId() );
			
			atualizaTotalDeContatosPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}	
	}
	
	private void atualizaTotalDeContatosPesquisa() {
		if(listaDeContatos != null)
			totalDeContatosPesquisa = listaDeContatos.size();
	}	
	
	public void buscar() {
		try {
			listaDeContatos = dao.pesquisar(nome, AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId());
			
			atualizaTotalDeContatosPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}
	
	public void removerContato(Long idContato) {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new ContatoDAOImpl(em);

		try {					
			em.getTransaction().begin();
			
			Contato contato = dao.buscarPor(idContato);
									
			dao.deletar(contato);
			
			if(em.getTransaction().isActive())
				em.getTransaction().commit();						
						
			MensagensJSFUtils.adicionarMsgInfo("Contato excluído com sucesso", "");
			
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
		
		dao = new ContatoDAOImpl();
	}
	
	public String redirecionarParaAlteracaoContato(Long idContato) {
		return "/pages/contato/manter_contato.jsf?faces-redirect=true&idContatoPesquisa=" + idContato;	
	}
}
