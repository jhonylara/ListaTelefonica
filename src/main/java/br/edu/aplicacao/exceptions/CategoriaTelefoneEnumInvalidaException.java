package br.edu.aplicacao.exceptions;

public class CategoriaTelefoneEnumInvalidaException extends Exception {

	private static final long serialVersionUID = 1L;

	public CategoriaTelefoneEnumInvalidaException() {
		super("Categoria/Tipo de Telefone inválida.");
	}
}
