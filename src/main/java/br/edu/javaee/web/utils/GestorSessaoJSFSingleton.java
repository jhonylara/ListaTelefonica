package br.edu.javaee.web.utils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Classe permite a manipulação da sessao JSF da aplicacao.
 * 
 * @author vagnercml
 */
public class GestorSessaoJSFSingleton {

	private static GestorSessaoJSFSingleton instance;

	public static GestorSessaoJSFSingleton getInstance() {
		if (instance == null) {
			instance = new GestorSessaoJSFSingleton();
		}

		return instance;
	}

	private GestorSessaoJSFSingleton() {
	}

	private ExternalContext currentExternalContext() {
		if (FacesContext.getCurrentInstance() == null) {
			throw new RuntimeException("O FacesContext não pode ser chamado fora de uma requisição HTTP");
		} else {
			return FacesContext.getCurrentInstance().getExternalContext();
		}
	}

	public void encerrarSessao() {
		currentExternalContext().invalidateSession();
	}

	public Object getAttribute(String nome) {
		return currentExternalContext().getSessionMap().get(nome);
	}
	
	public void setAttribute(String nome, Object valor) {
		currentExternalContext().getSessionMap().put(nome, valor);
	}
}