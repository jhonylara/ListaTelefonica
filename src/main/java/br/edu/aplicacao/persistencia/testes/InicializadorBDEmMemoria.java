package br.edu.aplicacao.persistencia.testes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.entidades.Endereco;
import br.edu.aplicacao.entidades.Grupo;
import br.edu.aplicacao.entidades.Telefone;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.enums.CategoriaEnderecoEnum;
import br.edu.aplicacao.enums.CategoriaTelefoneEnum;
import br.edu.aplicacao.enums.TipoEnderecoEnum;
import br.edu.aplicacao.enums.UnidadeFederacaoEnum;
import br.edu.aplicacao.persistencia.interfaces.IContatoDAO;
import br.edu.aplicacao.persistencia.interfaces.IGrupoDAO;
import br.edu.aplicacao.persistencia.interfaces.ITelefoneDAO;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.ContatoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.GrupoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.TelefoneDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.java.utils.DataEHoraUtils;
import br.edu.javaee.persistencia.EMFactorySingleton;

public class InicializadorBDEmMemoria {

	private IContatoDAO daoContato;
	
	private IUsuarioDAO daoUsuario;
	
	private ITelefoneDAO daoTelefone;
	
	private IGrupoDAO daoGrupo;
	
	private EntityManager em;
	
	public InicializadorBDEmMemoria() throws ParseException {
	
	
		em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		daoUsuario = new UsuarioDAOImpl(em);
		daoContato = new ContatoDAOImpl(em);
		daoTelefone = new TelefoneDAOImpl(em);
		daoGrupo = new GrupoDAOImpl(em);
	
		try {
			em.getTransaction().begin();
			
			preparaCadastroDeUsuarios();
			
			preparaCadastroDeContatosSemTelefonesEEndereco();
			
			preparaCadastroDeContatosComTelefones();
			
			preparaCadastroDeContatosSemTelefonesMasComEndereco();
			
			preparaCadastroDeContatosComTelefonesEComEndereco();
			
			preparaCadastroDeGruposSemVinculos();
			
			preparaCadastroDeContatosEGrupos();
			
			preparaCadastroDeContatoCompleto();
			
			preparaCadastroDeContatosComEndereco();
						
			if(em.getTransaction().isActive())
				em.getTransaction().commit();
			
		} catch (Exception e) {
			
			try {
				if(em.getTransaction().isActive())
					em.getTransaction().rollback();	
			} catch (Exception e2) {
				throw e2;
			}		
			
			throw e;			
		} finally {
			if(em != null && em.isOpen()) {
				em.close();
			}
		}		
	}

	private void preparaCadastroDeUsuarios() throws ParseException {
		
		Usuario usuario;
		
		usuario = new Usuario("adm", "123", "administrador", true);
		usuario.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("01/06/2018 12:00"));
		
		daoUsuario.inserir(usuario);
		
		usuario = new Usuario("usr1", "1", "usuário 1", false);
		usuario.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/06/2018 12:00"));
		
		daoUsuario.inserir(usuario);
		
		usuario = new Usuario("usr2", "2", "usuário 2", false);
		usuario.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("12/06/2018 12:00"));
		
		daoUsuario.inserir(usuario);
		
		usuario = new Usuario("usr3", "3", "usuário 3", false);
		usuario.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("01/05/2018 12:00"));
		
		daoUsuario.inserir(usuario);
		
		usuario = new Usuario("usr4", "4", "usuário 4", false);
		usuario.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/04/2018 12:00"));
		
		daoUsuario.inserir(usuario);
		
		em.flush();
	}
	
	private void preparaCadastroDeContatosSemTelefonesEEndereco() throws ParseException {
				
		Usuario donoDaListaContatos;		
		Contato contato;
		
		// Dono da Lista = ADM
		donoDaListaContatos = daoUsuario.buscarPor(1L);
				
		contato = new Contato("Contato 1 - sem tel e end", null, null, donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/02/2018 12:00"));
		daoContato.inserir(contato);
		
		contato = new Contato("Contato 2 - sem tel e end", "c2@hotmail.com", DataEHoraUtils.dataHoraStringParaDate("01/06/1970 12:00"), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/01/2018 12:00"));
		daoContato.inserir(contato);
		
		// Dono da Lista = USR
		donoDaListaContatos = daoUsuario.buscarPor(2L);
		
		contato = new Contato("Paulo José", "paulo@hotmail.com", DataEHoraUtils.dataHoraStringParaDate("10/06/1975 12:00"), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/06/2018 12:00"));
		daoContato.inserir(contato);
		
		contato = new Contato("Deize", null, DataEHoraUtils.dataHoraStringParaDate("30/06/1970 12:00"), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("15/06/2018 12:00"));
		daoContato.inserir(contato);
		
		contato = new Contato("José", "jsf@hotmail.com", DataEHoraUtils.dataHoraStringParaDate("20/02/1975 12:00"), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/03/2018 12:00"));
		daoContato.inserir(contato);
		em.flush();
	}

	private void preparaCadastroDeContatosComTelefones() throws ParseException {
		Usuario donoDaListaContatos;		
		Contato contato;
		Telefone telefone;
		
		// Dono da Lista = ADM
		donoDaListaContatos = daoUsuario.buscarPor(1L);
				
		contato = new Contato("Contato 3 - com tel e sem end", null, DataEHoraUtils.dataHoraStringParaDate("30/06/1970 12:00"), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/01/2018 12:00"));
		
		contato = daoContato.inserir(contato);
		
		contato.setTelefones(new ArrayList<Telefone>());
		
		telefone = new Telefone(null, null, 988775544L, CategoriaTelefoneEnum.CELULAR);
		telefone.setContato(contato);
		
		telefone = daoTelefone.inserir(telefone);
		
		contato.getTelefones().add(telefone);
		
		telefone = new Telefone(55, 41, 32006588L, CategoriaTelefoneEnum.COMERCIAL);
		telefone.setContato(contato);
		
		telefone = daoTelefone.inserir(telefone);
		
		contato.getTelefones().add(telefone);
		
		telefone = new Telefone(55, 41, 35274455L, CategoriaTelefoneEnum.RESIDENCIAL);		
		telefone.setContato(contato);
		
		telefone = daoTelefone.inserir(telefone);
		
		contato.getTelefones().add(telefone);
		em.flush();
	}
	
	private void preparaCadastroDeContatosSemTelefonesMasComEndereco() throws ParseException {
		Usuario donoDaListaContatos;		
		Contato contato;
		
		donoDaListaContatos = daoUsuario.buscarPor(1L);
		
		contato = new Contato("Contato 4 - sem tel e com end", null, DataEHoraUtils.dataHoraStringParaDate("15/05/2010 12:00"), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/11/2018 12:00"));
						
		Endereco endereco = new Endereco(
				CategoriaEnderecoEnum.RESIDENCIAL,
				TipoEnderecoEnum.RUA,
				"Curitiba",
				"Prof. Carlos de Almeida",
				"100",
				"AP 101",
				UnidadeFederacaoEnum.PR,
				"80000200");
		
		contato.setEndereco(endereco);
				
		contato = daoContato.inserir(contato);
		em.flush();
	}


	private void preparaCadastroDeContatosComTelefonesEComEndereco() throws ParseException {
		Usuario donoDaListaContatos;		
		Contato contato;
		Telefone telefone;
		
		donoDaListaContatos = daoUsuario.buscarPor(1L);
		
		contato = new Contato("Contato 5 - com tel e com end", null, DataEHoraUtils.dataHoraStringParaDate("05/03/1970 12:00"), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("06/06/2018 12:00"));
						
		Endereco endereco = new Endereco(
				CategoriaEnderecoEnum.RESIDENCIAL,
				TipoEnderecoEnum.RUA,
				"Curitiba",
				"Prof. Carlos de Almeida",
				"100",
				"AP 101",
				UnidadeFederacaoEnum.PR,
				"80520610");
		
		contato.setEndereco(endereco);
				
		contato = daoContato.inserir(contato);	
		
		contato.setTelefones(new ArrayList<Telefone>());
		
		telefone = new Telefone(55, 45, 900110011L, CategoriaTelefoneEnum.CELULAR);
		telefone.setContato(contato);
		
		telefone = daoTelefone.inserir(telefone);
		
		contato.getTelefones().add(telefone);	
		em.flush();
	}

	private void preparaCadastroDeGruposSemVinculos() {
		Usuario donoDaListaContatos;		
		
		donoDaListaContatos = daoUsuario.buscarPor(1L);
		
		Grupo g;
		
		g = new Grupo("Grupo 1", donoDaListaContatos);
		
		daoGrupo.inserir(g);
		
		g = new Grupo("Grupo 2", donoDaListaContatos);
		
		daoGrupo.inserir(g);		
		
		g = new Grupo("Grupo 3", donoDaListaContatos);
		
		daoGrupo.inserir(g);
		em.flush();
	}
	
	private void preparaCadastroDeContatosEGrupos() throws ParseException {
		Usuario donoDaListaContatos;
		Contato contato;
				
		donoDaListaContatos = daoUsuario.buscarPor(1L);
		
		contato = new Contato("Contato 6 - sem tel e end com grupo", null, DataEHoraUtils.dataHoraStringParaDate("10/12/2002 12:00"), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/08/2018 12:00"));
		
		contato.setGrupos(new ArrayList<Grupo>());
		
		List<Grupo> todosGruposCadastrados = daoGrupo.listarTodos();
		
		Grupo grupo;
		
		grupo = todosGruposCadastrados.get(0);
				
		contato.getGrupos().add( grupo );
		
		grupo = todosGruposCadastrados.get(2);
		
		contato.getGrupos().add( grupo );
		
		daoContato.inserir(contato);
		
		em.flush();
	}

	private void preparaCadastroDeContatoCompleto() throws ParseException {
		Usuario donoDaListaContatos;		
		Contato contato;
		Telefone telefone;
				
		donoDaListaContatos = daoUsuario.buscarPor(1L);
		
		contato = new Contato("Contato 7 (completo)", "email@teste.com", DataEHoraUtils.hoje(), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/08/2018 12:00"));
		
		// Endereço
		Endereco endereco = new Endereco(
				CategoriaEnderecoEnum.RESIDENCIAL,
				TipoEnderecoEnum.RUA,
				"Foz do Iguaçu",
				"Logradouro",
				"nº",
				"complemento",
				UnidadeFederacaoEnum.PR,
				"80000100");		
		contato.setEndereco(endereco);
		
		// Grupos
		contato.setGrupos(new ArrayList<Grupo>());		
		List<Grupo> todosGruposCadastrados = daoGrupo.listarTodos();		
		contato.setGrupos(todosGruposCadastrados);
		
		contato = daoContato.inserir(contato);
		
		// Telefones
		contato.setTelefones(new ArrayList<Telefone>());		
		telefone = new Telefone(55, 41, 900110011L, CategoriaTelefoneEnum.CELULAR);
		telefone.setContato(contato);
		
		telefone = daoTelefone.inserir(telefone);
		
		telefone = new Telefone(55, 41, 35102030L, CategoriaTelefoneEnum.RESIDENCIAL);
		telefone.setContato(contato);
		
		telefone = daoTelefone.inserir(telefone);
		
		contato.getTelefones().add(telefone);
		
		em.flush();
	}
	
	private void preparaCadastroDeContatosComEndereco() throws ParseException {
		Usuario donoDaListaContatos;		
		Contato contato;
				
		donoDaListaContatos = daoUsuario.buscarPor(1L);
		
		contato = new Contato("Contato 8 com Endereço", "email@teste.com", DataEHoraUtils.hoje(), donoDaListaContatos);
		contato.setDtInclusao(DataEHoraUtils.dataHoraStringParaDate("10/06/2018 12:00"));
		
		// Endereço
		Endereco endereco = new Endereco(
				CategoriaEnderecoEnum.RESIDENCIAL,
				TipoEnderecoEnum.RUA,
				"Porto Velho",
				"Logradouro",
				"nº",
				"complemento",
				UnidadeFederacaoEnum.RO,
				"80000100");		
		contato.setEndereco(endereco);
		
		// Grupos
		contato.setGrupos(new ArrayList<Grupo>());		
		List<Grupo> todosGruposCadastrados = daoGrupo.listarTodos();		
		contato.setGrupos(todosGruposCadastrados);
		
		contato = daoContato.inserir(contato);
	
		em.flush();		
	}

}
