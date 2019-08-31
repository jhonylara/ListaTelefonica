package br.edu.aplicacao.persistencia.interfaces.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.persistencia.interfaces.IContatoDAO;
import br.edu.javaee.persistencia.interfaces.impl.GenericaDAOImpl;

public class ContatoDAOImpl extends GenericaDAOImpl<Contato, Long> implements IContatoDAO {

	public ContatoDAOImpl() {
		super();
	}
	
	public ContatoDAOImpl(EntityManager em) {
		super(em);
	}

	public List<Contato> listarContatosPorUsuario(Long id) {
				
		StringBuilder sqlFrom = new StringBuilder("FROM " + this.entityClass.getName() + " WHERE ");
		
		sqlFrom.append("usuario.id");
		sqlFrom.append(" = :");
		sqlFrom.append("idUsuario");
		
		TypedQuery<Contato> consultaTipada = getEntityManager().createQuery(sqlFrom.toString(), this.entityClass);
		
		consultaTipada.setParameter("idUsuario", id);
		
		return consultaTipada.getResultList();
	}

	public List<Contato> pesquisar(String nome, Long idUsuarioLogado) {
		if(nome == null)
			return this.listarContatosPorUsuario(idUsuarioLogado);
		
		final String NOME_P1 = "nome";
		
		StringBuilder sqlFrom = new StringBuilder("FROM " + this.entityClass.getName() + " WHERE 1 = 1 ");
		
		sqlFrom.append(" AND usuario.id");
		sqlFrom.append(" = :");
		sqlFrom.append("idUsuario");
		
		sqlFrom.append(" AND lower(");		
		sqlFrom.append(NOME_P1);
		sqlFrom.append(") LIKE :");
		sqlFrom.append(NOME_P1);
		
		TypedQuery<Contato> consultaTipada = getEntityManager().createQuery(sqlFrom.toString(), this.entityClass);
		
		consultaTipada.setParameter("idUsuario", idUsuarioLogado);
		consultaTipada.setParameter(NOME_P1, "%" + nome.toLowerCase() + "%");
		
		return consultaTipada.getResultList();
	}

	@Override
	public Object qtdContatosPorDdd(Long idUsuarioLogado) {
	
		boolean deveFiltrarPorUsuario = (idUsuarioLogado != null);
		
		StringBuilder jpql = new StringBuilder(
				"SELECT tel.ddd, count(DISTINCT c.id) FROM " + this.entityClass.getName() + " c LEFT JOIN c.telefones tel WHERE 1 = 1 ");
		
		if(deveFiltrarPorUsuario) {
			jpql.append(" AND c.usuario.id");
			jpql.append(" = :");
			jpql.append("idUsuario");
		}
		
		jpql.append(" GROUP BY tel.ddd ");
		
		Query consulta = getEntityManager().createQuery(jpql.toString());
		
		if(deveFiltrarPorUsuario) {
			consulta.setParameter("idUsuario", idUsuarioLogado);
		}
			
		return consulta.getResultList();
	}

	@Override
	public Object qtdContatosPorLocalidade(Long idUsuarioLogado) {
		boolean deveFiltrarPorUsuario = (idUsuarioLogado != null);
		
		StringBuilder jpql = new StringBuilder(
				"SELECT c.endereco.localidade, count(DISTINCT c.id) FROM " + this.entityClass.getName() + " c WHERE 1 = 1 ");
		
		if(deveFiltrarPorUsuario) {
			jpql.append(" AND c.usuario.id");
			jpql.append(" = :");
			jpql.append("idUsuario");
		}
		
		jpql.append(" GROUP BY c.endereco.localidade ");
		
		Query consulta = getEntityManager().createQuery(jpql.toString());
		
		if(deveFiltrarPorUsuario) {
			consulta.setParameter("idUsuario", idUsuarioLogado);
		}
			
		return consulta.getResultList();
	}

	@Override
	public Long qtdTotalPorUsuario(Long idUsuarioLogado) {
		boolean deveFiltrarPorUsuario = (idUsuarioLogado != null);
		
		StringBuilder jpql = new StringBuilder("select count(*) FROM "  + entityClass.getName() + " WHERE 1 = 1  ");
		
		if(deveFiltrarPorUsuario) {
			jpql.append(" AND usuario.id");
			jpql.append(" = :");
			jpql.append("idUsuario");
		}
		
		Query queryTotal = getEntityManager().createQuery(jpql.toString());
		
		if(deveFiltrarPorUsuario) {
			queryTotal.setParameter("idUsuario", idUsuarioLogado);
		}
		
		Object objQtdTotal = queryTotal.getSingleResult();
		
		if(objQtdTotal != null) {
			if(objQtdTotal instanceof Integer) 
				return new Long((Integer)objQtdTotal);
			else if(objQtdTotal instanceof Long)
				return (Long) objQtdTotal;
		}
		
		return 0L; 
	}
}
