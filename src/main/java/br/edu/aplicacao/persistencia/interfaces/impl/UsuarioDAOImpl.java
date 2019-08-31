package br.edu.aplicacao.persistencia.interfaces.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.javaee.persistencia.interfaces.impl.GenericaDAOImpl;

public class UsuarioDAOImpl extends GenericaDAOImpl<Usuario, Long> implements IUsuarioDAO {

	public UsuarioDAOImpl() {
		super();
	}
	
	public UsuarioDAOImpl(EntityManager em) {
		super(em);
	}

	public List<Usuario> buscarPor(String login, String senha) {
		final String NOME_P1 = "login";
		final String NOME_P2 = "senha";
		
		StringBuilder sqlFrom = new StringBuilder("FROM " + this.entityClass.getName() + " WHERE ");
		
		sqlFrom.append(NOME_P1);
		sqlFrom.append(" LIKE :");
		sqlFrom.append(NOME_P1);
		
		sqlFrom.append(" AND ");
		
		sqlFrom.append(NOME_P2);
		sqlFrom.append(" LIKE :");
		sqlFrom.append(NOME_P2);
		
		TypedQuery<Usuario> consultaTipada = getEntityManager().createQuery(sqlFrom.toString(), this.entityClass);
		
		consultaTipada.setParameter(NOME_P1, login);
		consultaTipada.setParameter(NOME_P2, senha);
		
		return consultaTipada.getResultList();
	}

	public List<Usuario> pesquisar(String login, String nome) {
		if(login == null && nome == null)
			return this.listarTodos();
		
		final String NOME_P1 = "login";
		final String NOME_P2 = "nome";
		
		boolean deveFiltrarPorLogin	= (login != null) && (!login.isEmpty());
		boolean deveFiltrarPorNome 	= (nome != null) && (!nome.isEmpty()); 
		
		StringBuilder sqlFrom = new StringBuilder("FROM " + this.entityClass.getName() + " WHERE 1 = 1 ");
		
		if(deveFiltrarPorLogin) {
			sqlFrom.append(" AND lower(");
			sqlFrom.append(NOME_P1);
			sqlFrom.append(") LIKE :");
			sqlFrom.append(NOME_P1);
		}
		
		if(deveFiltrarPorNome) {
			sqlFrom.append(" AND lower(");		
			sqlFrom.append(NOME_P2);
			sqlFrom.append(") LIKE :");
			sqlFrom.append(NOME_P2);
		}
		
		TypedQuery<Usuario> consultaTipada = getEntityManager().createQuery(sqlFrom.toString(), this.entityClass);
		
		if(deveFiltrarPorLogin) {
			consultaTipada.setParameter(NOME_P1, "%" + login.toLowerCase() + "%");
		}
		
		if(deveFiltrarPorNome) {
			consultaTipada.setParameter(NOME_P2, "%" + nome.toLowerCase() + "%");
		}
		
		return consultaTipada.getResultList();
	}	
}
