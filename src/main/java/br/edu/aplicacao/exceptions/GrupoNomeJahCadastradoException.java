package br.edu.aplicacao.exceptions;

public class GrupoNomeJahCadastradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public GrupoNomeJahCadastradoException() {
		super("O grupo/nome informado já está cadastrado. Tente outro!");
	}
}
