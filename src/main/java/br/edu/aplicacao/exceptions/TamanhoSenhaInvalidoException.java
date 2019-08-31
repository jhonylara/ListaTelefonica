package br.edu.aplicacao.exceptions;

public class TamanhoSenhaInvalidoException extends Exception {

	private static final long serialVersionUID = 1L;

	public TamanhoSenhaInvalidoException() {
		super("O conteúdo do campo senha deve possuir no mínimo 3 e no maximo 8 caracteres.");
	}
}
