package br.edu.aplicacao.backingbeans.pessoa;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.entidades.Pessoa;
import br.edu.aplicacao.persistencia.interfaces.IPessoaDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.PessoaDAOImpl;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class PesquisarPessoaBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IPessoaDAO dao = new PessoaDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// campos para filtro/pesquisa
	private String nome;
	
	// campos usados no resultado da pesquisa
	private List<Pessoa> listaDePessoas;
	private Integer totalDePessoasPesquisa = 0;
	
	public PesquisarPessoaBB() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Integer getTotalDePessoasPesquisa() {
		return totalDePessoasPesquisa;
	}

	public void setTotalDePessoasPesquisa(Integer totalDePessoasPesquisa) {
		this.totalDePessoasPesquisa = totalDePessoasPesquisa;
	}

	public List<Pessoa> getListaDePessoas() {
		return listaDePessoas;
	}

	public void setListaDeUsuarios(List<Pessoa> listaDePessoas) {
		this.listaDePessoas = listaDePessoas;
	}

	public void preparar() {
		try {
			listaDePessoas = dao.listarTodos();
			
			atualizaTotalDeUsuariosPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}

	private void atualizaTotalDeUsuariosPesquisa() {
		if(listaDePessoas != null)
			totalDePessoasPesquisa = listaDePessoas.size();
	}	
	
	public void buscar() {
		try {
			listaDePessoas = dao.pesquisar(nome);
			
			atualizaTotalDeUsuariosPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}
	
	public String redirecionarParaManterUsuario(Long idPessoaSelecionada) {
		return "/pages/usuario/manter_usuario.jsf?faces-redirect=true&idUsuarioPesquisa=" + idPessoaSelecionada;
	}
	
	public void removerUsuario(Long idPessoaRemovida) {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new PessoaDAOImpl(em);
				
		try {					
			em.getTransaction().begin();
			
			Pessoa pessoa = dao.buscarPor(idPessoaRemovida);
									
			dao.deletar(pessoa);
		
			if(em.getTransaction().isActive())
				em.getTransaction().commit();						
			
			MensagensJSFUtils.adicionarMsgInfo("Pessoa excluida com sucesso", "");
			
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

		dao = new PessoaDAOImpl();
	}	
}
