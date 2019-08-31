package br.edu.javaee.persistencia.interfaces.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.persistencia.interfaces.IGenericaDAO;

/**
 * Implementação da DAO genérica
 * 
 * @author vagnercml
 * @param <T>
 * 		- entidade 
 * @param <PK> 
 * 		- id/pk da entidade
 */
public class GenericaDAOImpl<T, PK extends Serializable> implements IGenericaDAO<T, PK> {

	protected Class<T> entityClass;

	protected EntityManager entityManager = null;
	
		
	@SuppressWarnings("unchecked")
	public GenericaDAOImpl() {
		criarEntityClass();
	}
	
	public GenericaDAOImpl(EntityManager entityManager) {
		criarEntityClass();
		this.entityManager = entityManager; 
	}
	
	@SuppressWarnings("unchecked")
	private void criarEntityClass() {
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public EntityManager getEntityManager() {
		if(entityManager == null || (entityManager != null && !entityManager.isOpen()))
			entityManager = EMFactorySingleton.obterInstanciaUnica().criarEM();
		return entityManager;
	}
		
	public T inserir(T objeto) {		
		getEntityManager().persist(objeto);
		
		return objeto;
	}

	public T buscarPor(PK id) {
		return getEntityManager().find(this.entityClass, id);
	}

	public T alterar(T objeto) {
		return getEntityManager().merge(objeto);
	}

	public void deletar(T objeto) {
		getEntityManager().remove(objeto);
	}

	public List<T> listarTodos() {		
		return getEntityManager().createQuery("FROM " + entityClass.getName(), entityClass).getResultList();
	}
	
	public Long qtdTotal() {
		Query queryTotal = getEntityManager().createQuery("select count(*) FROM "  + entityClass.getName());
		
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