package br.edu.aplicacao.backingbeans.usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
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
import br.edu.aplicacao.exceptions.AlteracaoPerfilAdmInvalidaException;
import br.edu.aplicacao.exceptions.CamposObrigatoriosNaoInformadosException;
import br.edu.aplicacao.exceptions.ConfirmacaoSenhaInvalidaException;
import br.edu.aplicacao.exceptions.LoginJahCadastradoException;
import br.edu.aplicacao.exceptions.TamanhoSenhaInvalidoException;
import br.edu.aplicacao.listeners.GestorLogadoListener;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.java.utils.DataEHoraUtils;
import br.edu.java.utils.StringsUtils;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.GestorSessaoJSFSingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class ManterUsuarioBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IUsuarioDAO dao = new UsuarioDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Long id;	
	private String login;	
	private String senha;	
	private String senhaConfirmacao;
	private String nome;	
	private Boolean ehAdministrador;

	private Boolean ehAlteracaoPerfil = false;
	
	public ManterUsuarioBB() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getEhAdministrador() {
		return ehAdministrador;
	}

	public void setEhAdministrador(Boolean ehAdministrador) {
		this.ehAdministrador = ehAdministrador;
	}
		
	public String getSenhaConfirmacao() {
		return senhaConfirmacao;
	}

	public void setSenhaConfirmacao(String senhaConfirmacao) {
		this.senhaConfirmacao = senhaConfirmacao;
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
			
			validarLoginJahCadastradoInclusao();
			
			validarPreenchimentoSenha();
			
			incluirObjeto();
		
			MensagensJSFUtils.adicionarMsgInfo("Usuário incluído com sucesso", "");
		} catch (
				CamposObrigatoriosNaoInformadosException | 
				ConfirmacaoSenhaInvalidaException |
				LoginJahCadastradoException |
				TamanhoSenhaInvalidoException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}
	}

	public void salvarAlteracaoPerfil() {
		
		try {
			alterarPerfilObjeto();
			
			MensagensJSFUtils.adicionarMsgInfo("Usuário alterado com sucesso", "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}
	}

	private void alterarPerfilObjeto() {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new UsuarioDAOImpl(em);

		try {
			em.getTransaction().begin();
			
			Usuario usuario = dao.buscarPor(id);
			
			usuario.setEhAdministrador(ehAdministrador);
			
			dao.alterar(usuario);
			
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
		
		dao = new UsuarioDAOImpl();
	}
	
	public void prepararAlteracaoPerfil(Long idUsuarioPesquisa) {
		if(idUsuarioPesquisa != null) {
						
			Usuario usuarioCadastrado = dao.buscarPor(idUsuarioPesquisa);
			
			if(usuarioCadastrado == null) {
				String msgErro = "Código/Id de usuário inválido!"; 
				MensagensJSFUtils.adicionarMsgErro(msgErro, "");
			
				logger.error(msgErro);
				
				return;
			}
			
			this.id = idUsuarioPesquisa;
			this.nome = usuarioCadastrado.getNome();
			this.login = usuarioCadastrado.getLogin();
			this.senha = usuarioCadastrado.getSenha();
			this.senhaConfirmacao = this.senha;
			this.ehAdministrador = usuarioCadastrado.getEhAdministrador();
			this.ehAlteracaoPerfil = true;
		}
	}
		
	public void prepararAlteracaoMeusDados(Long idUsuario) {
		if(idUsuario != null) {
						
			Usuario usuarioCadastrado = dao.buscarPor(idUsuario);
			
			if(usuarioCadastrado == null) {
				String msgErro = "Código/Id de usuário inválido!"; 
				MensagensJSFUtils.adicionarMsgErro(msgErro, "");
			
				logger.error(msgErro);
				
				return;
			}
			
			this.id = idUsuario;
			this.nome = usuarioCadastrado.getNome();
			this.login = usuarioCadastrado.getLogin();
			this.senha = usuarioCadastrado.getSenha();
			this.senhaConfirmacao = this.senha;
			this.ehAdministrador = usuarioCadastrado.getEhAdministrador();
			this.ehAlteracaoPerfil = false;
		}
	}

	public String salvarMeusDados() {
		try {
			validarCamposObrigatorios();
			
			validarLoginJahCadastradoMeusDados();
			
			validarPreenchimentoSenha();
			
			validarAlteracaoPerfil();
			
			boolean trocouDeNome = verificarTrocaDeNome();
			
			alterarObjeto();
			
			if(trocouDeNome)
				tratarTrocaDeNome();
						
			MensagensJSFUtils.adicionarMsgInfo("Usuário alterado com sucesso", "");
			
			return "/pages/usuario/meus_dados_usuario.jsf?faces-redirect=true&idUsuario=" + this.id;
		} catch (
				CamposObrigatoriosNaoInformadosException | 
				ConfirmacaoSenhaInvalidaException |
				LoginJahCadastradoException |
				TamanhoSenhaInvalidoException |
				AlteracaoPerfilAdmInvalidaException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
			
			return "";
		} catch (Exception e) {
			
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
			
			return "";
		}
	}
	
	private void tratarTrocaDeNome() {
		AutenticadorBB.setarUsuarioLogadoDTONaSessao(null);
		
		Usuario usuarioCadastrado = dao.buscarPor(id);
		
		UsuarioLogadoDTO usuarioLogadoDTO = new UsuarioLogadoDTO(
				usuarioCadastrado.getId(), 
				usuarioCadastrado.getLogin(),
				usuarioCadastrado.getNome(),
				usuarioCadastrado.getEhAdministrador(), 
				GregorianCalendar.getInstance().getTime());
		
		AutenticadorBB.setarUsuarioLogadoDTONaSessao(usuarioLogadoDTO);
	}

	private boolean verificarTrocaDeNome() {
		Usuario usuarioCadastrado = dao.buscarPor(id);
		
		return (usuarioCadastrado.getNome().compareTo(nome) != 0);
	}

	private void validarAlteracaoPerfil() throws AlteracaoPerfilAdmInvalidaException {
		Usuario usuarioCadastrado = dao.buscarPor(id);
		
		if(
				(usuarioCadastrado.getEhAdministrador() == null && this.ehAdministrador != null)
				||
				(usuarioCadastrado.getEhAdministrador() != null && 
					(usuarioCadastrado.getEhAdministrador() == true && this.ehAdministrador == false)
					||
					(usuarioCadastrado.getEhAdministrador() == false && this.ehAdministrador == true)
				)
		)
			throw new AlteracaoPerfilAdmInvalidaException();
	}

	private void alterarObjeto() {
		dao = null;
				
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new UsuarioDAOImpl(em);

		try { 
			em.getTransaction().begin();
			
			Usuario usuario = dao.buscarPor(id);
			
			usuario.setNome(nome);
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setEhAdministrador(ehAdministrador);
			
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
		
		dao = new UsuarioDAOImpl();		
	}

	private void validarPreenchimentoSenha() throws ConfirmacaoSenhaInvalidaException, TamanhoSenhaInvalidoException {
		if(senha.compareTo(senhaConfirmacao) != 0)
			throw new ConfirmacaoSenhaInvalidaException();
		
		if(senha.length() < 3 || senha.length() > 8)
			throw new TamanhoSenhaInvalidoException();
	}

	private void validarLoginJahCadastradoInclusao() throws LoginJahCadastradoException {
				
		List<Usuario> resultadoPesquisaLogin = dao.pesquisar(login, null);
		
		if(resultadoPesquisaLogin != null && resultadoPesquisaLogin.size() > 0)
			throw new LoginJahCadastradoException();		
	}

	private void validarLoginJahCadastradoMeusDados() throws LoginJahCadastradoException {
		
		List<Usuario> resultadoPesquisaLogin = dao.pesquisar(login, null);
		
		if(resultadoPesquisaLogin != null && resultadoPesquisaLogin.size() > 1)
			throw new LoginJahCadastradoException();		
	}
	
	private void incluirObjeto() {
		dao = null;
					
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new UsuarioDAOImpl(em);
				
		try {		
			em.getTransaction().begin();	
			
			Usuario novoUsuario = new Usuario(login, senha, nome, ehAdministrador);
			
			novoUsuario.setDtInclusao(DataEHoraUtils.hoje());
			
			dao.inserir(novoUsuario);	
			
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
		
		dao = new UsuarioDAOImpl();
	}

	private void validarCamposObrigatorios() throws CamposObrigatoriosNaoInformadosException {
		List<String> camposNaoInformados = new ArrayList<String>();

		if(StringsUtils.ehStringVazia(nome))
			camposNaoInformados.add("nome");
		
		if(StringsUtils.ehStringVazia(login))
			camposNaoInformados.add("login");
		
		if(StringsUtils.ehStringVazia(senha))
			camposNaoInformados.add("senha");
		
		if(StringsUtils.ehStringVazia(senhaConfirmacao))
			camposNaoInformados.add("senha (confirmação)");
				
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
