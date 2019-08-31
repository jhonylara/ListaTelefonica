package br.edu.aplicacao.exceptions;

public class CamposObrigatoriosNaoInformadosException extends Exception {

	private static final long serialVersionUID = 1L;

	public CamposObrigatoriosNaoInformadosException(String listaCamposSepVirgula) {
		super("Campos obrigatórios não informados: " + listaCamposSepVirgula);
	}

	
}
