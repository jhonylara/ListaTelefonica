package br.edu.aplicacao.persistencia.interfaces;

import java.util.List;

import br.edu.aplicacao.entidades.Grupo;
import br.edu.javaee.persistencia.interfaces.IGenericaDAO;

public interface IGrupoDAO extends IGenericaDAO<Grupo, Long> {

	public List<Grupo> listarGruposPorUsuario(Long idUsuarioLogado);

	public List<Grupo> pesquisar(String nome, Long idUsuarioLogado);
}
