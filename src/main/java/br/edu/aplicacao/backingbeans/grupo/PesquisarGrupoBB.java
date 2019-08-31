package br.edu.aplicacao.backingbeans.grupo;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Grupo;
import br.edu.aplicacao.persistencia.interfaces.IGrupoDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.GrupoDAOImpl;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class PesquisarGrupoBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IGrupoDAO dao = new GrupoDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// campos para filtro/pesquisa
	private String nome;
	
	// campos usados no resultado da pesquisa
	private List<Grupo> listaPesquisa;
	private Integer totalPesquisa = 0;
	
	public PesquisarGrupoBB() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Grupo> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<Grupo> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public Integer getTotalPesquisa() {
		return totalPesquisa;
	}

	public void setTotalPesquisa(Integer totalPesquisa) {
		this.totalPesquisa = totalPesquisa;
	}
	
	public void preparar() {	
		try {
			UsuarioLogadoDTO usuarioLogadoDto = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
			
			listaPesquisa = dao.listarGruposPorUsuario( usuarioLogadoDto.getId() );
			
			atualizaTotalPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}	
	}
	
	private void atualizaTotalPesquisa() {
		if(listaPesquisa != null)
			totalPesquisa = listaPesquisa.size();
	}	
	
	public void buscar() {
		try {
			UsuarioLogadoDTO usuarioLogadoDto = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
			
			listaPesquisa = dao.pesquisar(nome, usuarioLogadoDto.getId());
			
			atualizaTotalPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}
	
	public void removerGrupo(Long id) {
		try {
			dao = null;
			
			EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
			
			dao = new GrupoDAOImpl(em);
					
			em.getTransaction().begin();
			
			Grupo grupo = dao.buscarPor(id);
									
			dao.deletar(grupo);
			
			em.getTransaction().commit();
			
			em.close();
			
			dao = new GrupoDAOImpl();
			
			MensagensJSFUtils.adicionarMsgInfo("Grupo excluído com sucesso", "");
			
			preparar();
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}		
	}
	
	public String redirecionarParaAlteracao(Long id) {
		return "/pages/grupo/manter_grupo.jsf?faces-redirect=true&idGrupoPesquisa=" + id;	
	}
}
