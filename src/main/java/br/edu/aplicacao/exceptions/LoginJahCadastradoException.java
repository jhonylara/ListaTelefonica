package br.edu.aplicacao.exceptions;

public class LoginJahCadastradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoginJahCadastradoException() {
		super("O login informado já está cadastrado. Tente outro!");
	}
}


