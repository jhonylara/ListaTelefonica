package br.edu.aplicacao.persistencia.interfaces.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.aplicacao.entidades.Pessoa;
import br.edu.aplicacao.persistencia.interfaces.IPessoaDAO;
import br.edu.javaee.persistencia.interfaces.impl.GenericaDAOImpl;

public class PessoaDAOImpl extends GenericaDAOImpl<Pessoa, Long> implements IPessoaDAO {

	public PessoaDAOImpl() {
		super();
	}
	
	public PessoaDAOImpl(EntityManager em) {
		super(em);
	}

	public List<Pessoa> buscarPor(String nome) {
		final String NOME = "nome";
		
		StringBuilder sqlFrom = new StringBuilder("FROM " + this.entityClass.getName() + " WHERE ");
		
		sqlFrom.append(NOME);
		sqlFrom.append(" LIKE :");
		sqlFrom.append(NOME);
		
		
		TypedQuery<Pessoa> consultaTipada = getEntityManager().createQuery(sqlFrom.toString(), this.entityClass);
		
		consultaTipada.setParameter(NOME, nome);
		
		return consultaTipada.getResultList();
	}

	public List<Pessoa> pesquisar(String nome) {
		if(nome == null)
			return this.listarTodos();
		
		final String NOME = "nome";
		
		boolean deveFiltrarPorNome 	= (nome != null) && (!nome.isEmpty()); 
		
		StringBuilder sqlFrom = new StringBuilder("FROM " + this.entityClass.getName() + " WHERE 1 = 1 ");
		
		if(deveFiltrarPorNome) {
			sqlFrom.append(" AND lower(");		
			sqlFrom.append(NOME);
			sqlFrom.append(") LIKE :");
			sqlFrom.append(NOME);
		}
		
		TypedQuery<Pessoa> consultaTipada = getEntityManager().createQuery(sqlFrom.toString(), this.entityClass);
		
		if(deveFiltrarPorNome) {
			consultaTipada.setParameter(NOME, "%" + nome.toLowerCase() + "%");
		}
		
		return consultaTipada.getResultList();
	}	
}
