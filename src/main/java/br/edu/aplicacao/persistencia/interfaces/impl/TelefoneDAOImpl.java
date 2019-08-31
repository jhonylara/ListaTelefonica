package br.edu.aplicacao.persistencia.interfaces.impl;

import javax.persistence.EntityManager;

import br.edu.aplicacao.entidades.Telefone;
import br.edu.aplicacao.persistencia.interfaces.ITelefoneDAO;
import br.edu.javaee.persistencia.interfaces.impl.GenericaDAOImpl;

public class TelefoneDAOImpl extends GenericaDAOImpl<Telefone, Long> implements ITelefoneDAO {

	public TelefoneDAOImpl() {
		super();
	}
	
	public TelefoneDAOImpl(EntityManager em) {
		super(em);
	}

}
