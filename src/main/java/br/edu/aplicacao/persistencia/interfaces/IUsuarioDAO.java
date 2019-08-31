package br.edu.aplicacao.persistencia.interfaces;

import java.util.List;

import br.edu.aplicacao.entidades.Usuario;
import br.edu.javaee.persistencia.interfaces.IGenericaDAO;

public interface IUsuarioDAO extends IGenericaDAO<Usuario, Long> {

	public List<Usuario> buscarPor(String login, String senha);

	public List<Usuario> pesquisar(String login, String nome);
}
