package br.edu.aplicacao.backingbeans;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.listeners.GestorLogadoListener;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.javaee.web.utils.GestorSessaoJSFSingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class AutenticadorBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String login;
	private String senha;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public AutenticadorBB() {
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
	
	public String realizarLogin() {
		try {
			String proximaPagina = "";
			
			IUsuarioDAO daoUsuario = new UsuarioDAOImpl();
			
			List<Usuario> usuarios = daoUsuario.buscarPor(login, senha);
			
			if(usuarios != null) {
				
				if(usuarios.size() == 1) {
									
					adicionarUsuarioLogadoDTONaSessao(usuarios.get(0));
					
					proximaPagina = "inicio?faces-redirect=true";				
				} else if(usuarios.size() > 1) {
					MensagensJSFUtils.adicionarMsgErro("Há dois usuários cadastrados com o mesmo login e a mesma senha.", "");
				} else {
					MensagensJSFUtils.adicionarMsgErro("Ususário/senha não cadastrado.", "");
				}
			} else {
				MensagensJSFUtils.adicionarMsgErro("Ususário/senha não cadastrado.", "");
			}
			
			return proximaPagina;			
		} catch (Exception e) {
			String msgErro = "ERRO interno e/ou de sistema: contate o administrador!"; 
			MensagensJSFUtils.adicionarMsgErro(msgErro, "");
			
			logger.error(msgErro, e);		

			return "";
		}
	}

	private void adicionarUsuarioLogadoDTONaSessao(Usuario usuario) {
		UsuarioLogadoDTO usuarioLogadoDTO = new UsuarioLogadoDTO(
				usuario.getId(), 
				usuario.getLogin(),
				usuario.getNome(),
				usuario.getEhAdministrador(), 
				GregorianCalendar.getInstance().getTime());
				
		setarUsuarioLogadoDTONaSessao(usuarioLogadoDTO);
	}

	public String realizarLogout() {
				
		setarUsuarioLogadoDTONaSessao(null);
		
		return "/pages/login.jsf?faces-redirect=true";
	}
	
	public String getNomeUsuarioLogado() {
		UsuarioLogadoDTO dto = obterUsuarioLogadoDTODaSessao();
		if(dto != null)
			return dto.getNome();
		return "";
	}
	
	public Boolean getEhAdministradorUsuarioLogado() {
		UsuarioLogadoDTO dto = obterUsuarioLogadoDTODaSessao();
		
		return (dto != null && dto.getEhAdministrador());
	}
	
	public Boolean idInformadoEhUsuarioLogado(Long idInformado) {
		UsuarioLogadoDTO dto = obterUsuarioLogadoDTODaSessao();
		
		return (dto != null && dto.getId().longValue() == idInformado.longValue());
	}
	
	public Long getIdUsuarioLogado() {
		UsuarioLogadoDTO dto = obterUsuarioLogadoDTODaSessao();
		
		if(dto != null && dto.getId() != null)
			return dto.getId();
		return 0L;
	}
	
	public static UsuarioLogadoDTO obterUsuarioLogadoDTODaSessao() {
		UsuarioLogadoDTO dto = (UsuarioLogadoDTO) GestorSessaoJSFSingleton.getInstance().getAttribute(GestorLogadoListener.USUARIO_LOGADO);
		return dto;
	}
	
	public static void setarUsuarioLogadoDTONaSessao(UsuarioLogadoDTO usuarioLogadoDTO) {
		GestorSessaoJSFSingleton.getInstance().setAttribute(GestorLogadoListener.USUARIO_LOGADO, usuarioLogadoDTO);
	}	
}
