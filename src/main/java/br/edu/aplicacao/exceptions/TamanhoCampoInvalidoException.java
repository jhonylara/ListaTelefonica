package br.edu.aplicacao.exceptions;

public class TamanhoCampoInvalidoException extends Exception {

	private static final long serialVersionUID = 1L;

	public TamanhoCampoInvalidoException(String campo, int tamanhoMin, int tamanhoMax) {
		super((tamanhoMin > 0)?campo + ": o tamanho mínimo é de \"" + tamanhoMin + "\".":campo + ": o tamanho máximo é de \"" + tamanhoMax + "\".");
	}
}
