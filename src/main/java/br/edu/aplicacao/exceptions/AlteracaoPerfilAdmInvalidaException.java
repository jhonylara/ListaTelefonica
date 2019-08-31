package br.edu.aplicacao.exceptions;

public class AlteracaoPerfilAdmInvalidaException extends Exception {

	public AlteracaoPerfilAdmInvalidaException() {
		super("O usário não pode alterar seu próprio perfil. Outro usuário (adm) deve fazer isso!");
	}
}
