package br.edu.javaee.persistencia.interfaces;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * Interface para criação de uma DAO genérica para inserir, buscar (por id), alterar e
 * 		deletar/apagar objetos em banco de dados relacional
 * 
 * @author vagnercml
 * @param <T>
 * 		- entidade 
 * @param <PK> 
 * 		- id/pk da entidade
 */
public interface IGenericaDAO<T, PK extends Serializable> {

	/**
	 * Insere de forma genérica um objeto do tipo T na base de dados
	 * 
	 * @param obj
	 * @return T 
	 */
	public T inserir(T objeto);

	/**
	 * Retorna um T a partir do valor da chave primária
	 * 
	 * @param id
	 * @return T 
	 */
	public T buscarPor(PK id);

	/**
	 * Altera o objeto informado como parâmetro
	 * 
	 * @param obj
	 * @return T 
	 */
	public T alterar(T objeto);

	/**
	 * Deleta/apaga o objeto informado como parâmetro
	 * 
	 * @param objeto
	 */
	public void deletar(T objeto);
	
	/**
	 * Lista todos os objetos/registros
	 * 
	 * @param objeto
	 */
	public List<T> listarTodos();
	
	/**
	 * Retorna a qtd total de registros/objetos
	 * 
	 * @return
	 */
	public Long qtdTotal();
}