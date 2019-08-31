package br.edu.aplicacao.exceptions;

public class NomeJahCadastradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public NomeJahCadastradoException() {
		super("O nome informado j� esta cadastrado. Tente outro!");
	}
}


