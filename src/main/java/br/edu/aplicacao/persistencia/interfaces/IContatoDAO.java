package br.edu.aplicacao.persistencia.interfaces;

import java.util.List;

import br.edu.aplicacao.entidades.Contato;
import br.edu.javaee.persistencia.interfaces.IGenericaDAO;

public interface IContatoDAO extends IGenericaDAO<Contato, Long> {

	public List<Contato> listarContatosPorUsuario(Long id);

	public List<Contato> pesquisar(String nome, Long idUsuarioLogado);
	
	public Object qtdContatosPorDdd(Long idUsuarioLogado);

	public Object qtdContatosPorLocalidade(Long idUsuarioLogado);

	public Long qtdTotalPorUsuario(Long idUsuarioLogado);
}
