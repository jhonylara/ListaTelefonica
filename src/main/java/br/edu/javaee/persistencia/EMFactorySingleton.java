package br.edu.javaee.persistencia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe para criar/retornar uma única fábrica de gerenciador de entidade 
 * 	(ou EntityManagerFactory).
 * 
 * @author vagnercml
 */
public class EMFactorySingleton {

	private static final String NOME_UNIDADE_PERSISTENCIA = "puaplicacao";

	private EntityManagerFactory fabricaDeGerenciadorDeEntidade = null;
	
	private static EMFactorySingleton instanciaUnica = new EMFactorySingleton();
	
	private EMFactorySingleton() {
		fabricaDeGerenciadorDeEntidade = Persistence.createEntityManagerFactory(NOME_UNIDADE_PERSISTENCIA);
	}
	
	public static EMFactorySingleton obterInstanciaUnica() {
		return instanciaUnica;
	}
	
	public EntityManager criarEM() {
		return fabricaDeGerenciadorDeEntidade.createEntityManager();
	}
}
