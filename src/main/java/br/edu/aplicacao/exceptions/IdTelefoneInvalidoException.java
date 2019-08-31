package br.edu.aplicacao.exceptions;

public class IdTelefoneInvalidoException extends Exception {

	private static final long serialVersionUID = 1L;

	public IdTelefoneInvalidoException() {
		super("Id. do telefone inválido.");
	}
}
