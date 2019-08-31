package br.edu.aplicacao.exceptions;

public class ConfirmacaoSenhaInvalidaException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfirmacaoSenhaInvalidaException() {
		super("O conteúdo informado nos campos Senha e Senha (confirmação) não é o mesmo.");
	}
}
