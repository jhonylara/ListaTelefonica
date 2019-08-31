package br.edu.aplicacao.backingbeans.pessoa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.PessoaLogadaDTO;
import br.edu.aplicacao.entidades.Pessoa;
import br.edu.aplicacao.exceptions.CamposObrigatoriosNaoInformadosException;
import br.edu.aplicacao.exceptions.NomeJahCadastradoException;
import br.edu.aplicacao.persistencia.interfaces.IPessoaDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.PessoaDAOImpl;
import br.edu.java.utils.StringsUtils;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class ManterPessoaBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private IPessoaDAO dao = new PessoaDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Long id;	
	private String nome;	
	private Boolean genero;	
	private Date dtNascimento;
	
	private Boolean ehAlteracaoPerfil = false;
	
	public ManterPessoaBB() {
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

	public Boolean getEhAlteracaoPerfil() {
		return ehAlteracaoPerfil;
	}

	public void setEhAlteracaoPerfil(Boolean ehAlteracaoPerfil) {
		this.ehAlteracaoPerfil = ehAlteracaoPerfil;
	}
	
	public void incluir() {

		try {
			validarCamposObrigatorios();
			
			validarNomeJahCadastradoInclusao();
			
			incluirObjeto();
		
			MensagensJSFUtils.adicionarMsgInfo("Pessoa incluida com sucesso", "");
		} catch (
				CamposObrigatoriosNaoInformadosException | 
				NomeJahCadastradoException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}
	}

	public void salvarAlteracaoPerfil() {
		
		try {
			alterarPerfilObjeto();
			
			MensagensJSFUtils.adicionarMsgInfo("Pessoa alterado com sucesso", "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}
	}

	private void alterarPerfilObjeto() {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new PessoaDAOImpl(em);

		try {
			em.getTransaction().begin();
			
			Pessoa pessoa = dao.buscarPor(id);			
			
			dao.alterar(pessoa);
			
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
		
		dao = new PessoaDAOImpl();
	}
	
	public void prepararAlteracaoPerfil(Long idPessoaPesquisa) {
		if(idPessoaPesquisa != null) {
						
			Pessoa pessoaCadastrada = dao.buscarPor(idPessoaPesquisa);
			
			if(pessoaCadastrada == null) {
				String msgErro = "Codigo/Id da pessoa invalida!"; 
				MensagensJSFUtils.adicionarMsgErro(msgErro, "");
			
				logger.error(msgErro);
				
				return;
			}
			
			this.id = idPessoaPesquisa;
			this.nome = pessoaCadastrada.getNome();
			this.genero = pessoaCadastrada.getGenero();
			this.dtNascimento = pessoaCadastrada.getDtNascimento();
			this.ehAlteracaoPerfil = true;
		}
	}
		
	public void prepararAlteracaoMeusDados(Long idPessoa) {
		if(idPessoa != null) {
						
			Pessoa pessoaCadastrada = dao.buscarPor(idPessoa);
			
			if(pessoaCadastrada == null) {
				String msgErro = "Codigo/Id da pessoa invalida!"; 
				MensagensJSFUtils.adicionarMsgErro(msgErro, "");
			
				logger.error(msgErro);
				
				return;
			}
			
			this.id = idPessoa;
			this.nome = pessoaCadastrada.getNome();
			this.genero = pessoaCadastrada.getGenero();
			this.dtNascimento = pessoaCadastrada.getDtNascimento();
			this.ehAlteracaoPerfil = false;
			
		}
	}

	public String salvarMeusDados() {
		try {
			validarCamposObrigatorios();
			
			boolean trocouDeNome = verificarTrocaDeNome();
			
			alterarObjeto();
			
			if(trocouDeNome)
				tratarTrocaDeNome();
						
			MensagensJSFUtils.adicionarMsgInfo("Pessoa alterado com sucesso", "");
			
			return "/pages/pessoa/meus_dados_usuario.jsf?faces-redirect=true&idUsuario=" + this.id;
		} catch (
				CamposObrigatoriosNaoInformadosException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
			
			return "";
		} catch (Exception e) {
			
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
			
			return "";
		}
	}
	
	private void tratarTrocaDeNome() {
		AutenticadorBB.setarUsuarioLogadoDTONaSessao(null);
		
		Pessoa pessoaCadastrada = dao.buscarPor(id);
		
		PessoaLogadaDTO pessoaLogadaDTO = new PessoaLogadaDTO(
				pessoaCadastrada.getId(), 
				pessoaCadastrada.getNome(),
				pessoaCadastrada.getGenero(),
				pessoaCadastrada.getDtNascimento()
				);
		
		//AutenticadorBB.setarPessoaLogadaDTONaSessao(pessoaLogadaDTO);
	}

	private boolean verificarTrocaDeNome() {
		Pessoa pessoaCadastrada = dao.buscarPor(id);
		
		return (pessoaCadastrada.getNome().compareTo(nome) != 0);
	}


	private void alterarObjeto() {
		dao = null;
				
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new PessoaDAOImpl(em);

		try { 
			em.getTransaction().begin();
			
			Pessoa pessoa = dao.buscarPor(id);
			
			pessoa.setNome(nome);
			pessoa.setGenero(genero);
			pessoa.setDtNascimento(dtNascimento);
			
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
		
		dao = new PessoaDAOImpl();		
	}


	private void validarNomeJahCadastradoInclusao() throws NomeJahCadastradoException {
				
		List<Pessoa> resultadoPesquisaNome = dao.pesquisar(nome);
		
		if(resultadoPesquisaNome != null && resultadoPesquisaNome.size() > 0)
			throw new NomeJahCadastradoException();		
	}
	
	private void incluirObjeto() {
		dao = null;
					
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new PessoaDAOImpl(em);
				
		try {		
			em.getTransaction().begin();	
			
			Pessoa novaPessoa = new Pessoa(nome, genero, dtNascimento);
			
			dao.inserir(novaPessoa);	
			
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
		
		dao = new PessoaDAOImpl();
	}

	private void validarCamposObrigatorios() throws CamposObrigatoriosNaoInformadosException {
		List<String> camposNaoInformados = new ArrayList<String>();

		if(StringsUtils.ehStringVazia(nome))
			camposNaoInformados.add("nome");
				
		String listaCamposSepVirgula = "";
		
		if(camposNaoInformados.size() > 0) {
		
			int indice = 1;
			
			for (Iterator<String> iterator = camposNaoInformados.iterator(); iterator.hasNext();) {
				String nomeCampoNaoInformado = (String) iterator.next();
								
				if(indice < camposNaoInformados.size())
					listaCamposSepVirgula = listaCamposSepVirgula + nomeCampoNaoInformado + ", ";
				else
					listaCamposSepVirgula = listaCamposSepVirgula + nomeCampoNaoInformado;
				
				indice++;
			}
		}
		
		if(!"".equalsIgnoreCase(listaCamposSepVirgula))
			throw new CamposObrigatoriosNaoInformadosException(listaCamposSepVirgula);
	}
	
	
}
