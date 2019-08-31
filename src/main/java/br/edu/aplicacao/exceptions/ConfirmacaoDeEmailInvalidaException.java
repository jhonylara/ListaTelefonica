package br.edu.aplicacao.exceptions;

public class ConfirmacaoDeEmailInvalidaException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfirmacaoDeEmailInvalidaException() {
		super("Email e Email (confirmação) não são iguais.");
	}
}
