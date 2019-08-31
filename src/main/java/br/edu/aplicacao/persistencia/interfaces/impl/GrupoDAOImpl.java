package br.edu.aplicacao.persistencia.interfaces.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.entidades.Grupo;
import br.edu.aplicacao.persistencia.interfaces.IGrupoDAO;
import br.edu.javaee.persistencia.interfaces.impl.GenericaDAOImpl;

public class GrupoDAOImpl extends GenericaDAOImpl<Grupo, Long> implements IGrupoDAO {

	public GrupoDAOImpl() {
		super();
	}
	
	public GrupoDAOImpl(EntityManager em) {
		super(em);
	}
	
	public List<Grupo> listarGruposPorUsuario(Long idUsuarioLogado) {
		
		StringBuilder sqlFrom = new StringBuilder("FROM " + this.entityClass.getName() + " WHERE ");
		
		sqlFrom.append("usuario.id");
		sqlFrom.append(" = :");
		sqlFrom.append("idUsuario");
		
		TypedQuery<Grupo> consultaTipada = getEntityManager().createQuery(sqlFrom.toString(), this.entityClass);
		
		consultaTipada.setParameter("idUsuario", idUsuarioLogado);
		
		return consultaTipada.getResultList();
	}
	
	public List<Grupo> pesquisar(String nome, Long idUsuarioLogado) {
		if(nome == null)
			return this.listarGruposPorUsuario(idUsuarioLogado);
		
		final String NOME_P1 = "nome";
		
		StringBuilder sqlFrom = new StringBuilder("FROM " + this.entityClass.getName() + " WHERE 1 = 1 ");
		
		sqlFrom.append(" AND usuario.id");
		sqlFrom.append(" = :");
		sqlFrom.append("idUsuario");
		
		sqlFrom.append(" AND lower(");		
		sqlFrom.append(NOME_P1);
		sqlFrom.append(") LIKE :");
		sqlFrom.append(NOME_P1);
		
		TypedQuery<Grupo> consultaTipada = getEntityManager().createQuery(sqlFrom.toString(), this.entityClass);
		
		consultaTipada.setParameter("idUsuario", idUsuarioLogado);
		consultaTipada.setParameter(NOME_P1, "%" + nome.toLowerCase() + "%");
		
		return consultaTipada.getResultList();
	}
	
}
