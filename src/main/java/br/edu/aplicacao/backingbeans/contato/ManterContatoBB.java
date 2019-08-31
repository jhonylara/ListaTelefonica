package br.edu.aplicacao.backingbeans.contato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.ItemListaTelefoneDTO;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.entidades.Endereco;
import br.edu.aplicacao.entidades.Grupo;
import br.edu.aplicacao.entidades.Telefone;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.enums.CategoriaEnderecoEnum;
import br.edu.aplicacao.enums.CategoriaTelefoneEnum;
import br.edu.aplicacao.enums.TipoEnderecoEnum;
import br.edu.aplicacao.enums.UnidadeFederacaoEnum;
import br.edu.aplicacao.exceptions.CamposObrigatoriosNaoInformadosException;
import br.edu.aplicacao.exceptions.CategoriaTelefoneEnumInvalidaException;
import br.edu.aplicacao.exceptions.ConfirmacaoDeEmailInvalidaException;
import br.edu.aplicacao.exceptions.IdTelefoneInvalidoException;
import br.edu.aplicacao.exceptions.TamanhoCampoInvalidoException;
import br.edu.aplicacao.persistencia.interfaces.IContatoDAO;
import br.edu.aplicacao.persistencia.interfaces.IGrupoDAO;
import br.edu.aplicacao.persistencia.interfaces.ITelefoneDAO;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.ContatoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.GrupoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.TelefoneDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.java.utils.DataEHoraUtils;
import br.edu.java.utils.StringsUtils;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

/**
 * Esta classe auxilia na implementação dos seguintes requisitos/cenários:
 *  
 * - Pesquisar, Incluir, Alterar e Excluir contato(s) associado ao usuário logado
 * 		no sistema.
 * 
 * - Incluir, Alterar e/ou Excluir telefone(s) para/de um determinado contato. 
 * 		Atenção: a inclusão, a alteração e a exclusão de um determinado telefone 
 * 			na lista de telefones são mantidas em memória, elas são apenas efetivadas em 
 * 			banco de dados quando o usuário clica em Incluir ou Alterar do contato. 
 * 
 * @author vagner.l@uninter.com
 * @author vagnercml@hotmail.com
 *
 */
@Named
@ViewScoped
public class ManterContatoBB implements Serializable {

	private static final long serialVersionUID = 1L;
		
	private IContatoDAO dao = new ContatoDAOImpl();
	
	private IUsuarioDAO daoUsuario = new UsuarioDAOImpl();
	
	private ITelefoneDAO daoTelefone = new TelefoneDAOImpl();
	
	private IGrupoDAO daoGrupo = new GrupoDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Boolean ehAlteracao = false;
	
	private Long id;	
	private String nome;	
	private String email;	
	private String emailConfirmacao;
	private Date dtNascimento;
		
	private Integer telefoneIndex = null;
	private Integer telefoneCodigoArea;
	private Integer telefoneDdd;
	private Long telefoneNumero;
	private Integer telefoneCategoriaCodigo;
	
	private CategoriaTelefoneEnum[] telefoneCategoriaValores;	
	private List<ItemListaTelefoneDTO> itensTelefoneListaDto;	
	private List<ItemListaTelefoneDTO> itensTelefoneListaDtoExcluidos;
	private int sequencia = 1;
	
	private CategoriaEnderecoEnum[] enderecoCategoriaValores;	
	private Integer enderecoCategoriaCodigo;	
	private TipoEnderecoEnum[] enderecoTipoValores;
	private Integer enderecoTipoCodigo;	
	private UnidadeFederacaoEnum[] enderecoUfValores;	
	private Integer enderecoUfCodigo;	
	private String enderecoLogradouro;	
	private String enderecoNumeroOuLoteamento;	
	private String enderecoLocalidade;	
	private String enderecoComplemento;	
	private String enderecoCep;
	
	private List<Grupo> gruposDisponiveis;
	private Long[] idsGruposSelecionados;
				
	public ManterContatoBB() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public String getEmailConfirmacao() {
		return emailConfirmacao;
	}

	public void setEmailConfirmacao(String emailConfirmacao) {
		this.emailConfirmacao = emailConfirmacao;
	}

	public Boolean getEhAlteracao() {
		return ehAlteracao;
	}

	public void setEhAlteracao(Boolean ehAlteracao) {
		this.ehAlteracao = ehAlteracao;
	}
	
	public Integer getTelefoneCodigoArea() {
		return telefoneCodigoArea;
	}

	public void setTelefoneCodigoArea(Integer telefoneCodigoArea) {
		this.telefoneCodigoArea = telefoneCodigoArea;
	}

	public Integer getTelefoneDdd() {
		return telefoneDdd;
	}

	public void setTelefoneDdd(Integer telefoneDdd) {
		this.telefoneDdd = telefoneDdd;
	}

	public Long getTelefoneNumero() {
		return telefoneNumero;
	}

	public void setTelefoneNumero(Long telefoneNumero) {
		this.telefoneNumero = telefoneNumero;
	}

	public Integer getTelefoneCategoriaCodigo() {
		return telefoneCategoriaCodigo;
	}

	public void setTelefoneCategoriaCodigo(Integer categoriaCodigo) {
		this.telefoneCategoriaCodigo = categoriaCodigo;
	}

	public CategoriaTelefoneEnum[] getTelefoneCategoriaValores() {
		return telefoneCategoriaValores;
	}

	public void setTelefoneCategoriaValores(CategoriaTelefoneEnum[] telefoneCategoriaValores) {
		this.telefoneCategoriaValores = telefoneCategoriaValores;
	}

	public Integer getTelefoneIndex() {
		return telefoneIndex;
	}

	public void setTelefoneIndex(Integer telefoneIndex) {
		this.telefoneIndex = telefoneIndex;
	}
	
	public List<ItemListaTelefoneDTO> getItensTelefoneListaDto() {
		if(itensTelefoneListaDto == null)
			this.setItensTelefoneListaDto(new ArrayList<ItemListaTelefoneDTO>());
		
		return itensTelefoneListaDto;
	}

	public void setItensTelefoneListaDto(List<ItemListaTelefoneDTO> telefoneLista) {
		this.itensTelefoneListaDto = telefoneLista;
	}

	public CategoriaEnderecoEnum[] getEnderecoCategoriaValores() {
		return enderecoCategoriaValores;
	}

	public void setEnderecoCategoriaValores(CategoriaEnderecoEnum[] enderecoCategoriaValores) {
		this.enderecoCategoriaValores = enderecoCategoriaValores;
	}

	public Integer getEnderecoCategoriaCodigo() {
		return enderecoCategoriaCodigo;
	}

	public void setEnderecoCategoriaCodigo(Integer enderecoCategoriaCodigo) {
		this.enderecoCategoriaCodigo = enderecoCategoriaCodigo;
	}

	public TipoEnderecoEnum[] getEnderecoTipoValores() {
		return enderecoTipoValores;
	}

	public void setEnderecoTipoValores(TipoEnderecoEnum[] enderecoTipoValores) {
		this.enderecoTipoValores = enderecoTipoValores;
	}

	public Integer getEnderecoTipoCodigo() {
		return enderecoTipoCodigo;
	}

	public void setEnderecoTipoCodigo(Integer enderecoTipoCodigo) {
		this.enderecoTipoCodigo = enderecoTipoCodigo;
	}

	public UnidadeFederacaoEnum[] getEnderecoUfValores() {
		return enderecoUfValores;
	}

	public void setEnderecoUfValores(UnidadeFederacaoEnum[] enderecoUfValores) {
		this.enderecoUfValores = enderecoUfValores;
	}

	public Integer getEnderecoUfCodigo() {
		return enderecoUfCodigo;
	}

	public void setEnderecoUfCodigo(Integer enderecoUfCodigo) {
		this.enderecoUfCodigo = enderecoUfCodigo;
	}

	public String getEnderecoLogradouro() {
		return enderecoLogradouro;
	}

	public void setEnderecoLogradouro(String enderecoLogradouro) {
		this.enderecoLogradouro = enderecoLogradouro;
	}

	public String getEnderecoNumeroOuLoteamento() {
		return enderecoNumeroOuLoteamento;
	}

	public void setEnderecoNumeroOuLoteamento(String enderecoNumeroOuLoteamento) {
		this.enderecoNumeroOuLoteamento = enderecoNumeroOuLoteamento;
	}

	public String getEnderecoLocalidade() {
		return enderecoLocalidade;
	}

	public void setEnderecoLocalidade(String enderecoLocalidade) {
		this.enderecoLocalidade = enderecoLocalidade;
	}

	public String getEnderecoComplemento() {
		return enderecoComplemento;
	}

	public void setEnderecoComplemento(String enderecoComplemento) {
		this.enderecoComplemento = enderecoComplemento;
	}

	public String getEnderecoCep() {
		return enderecoCep;
	}

	public void setEnderecoCep(String enderecoCep) {
		this.enderecoCep = enderecoCep;
	}

	public List<Grupo> getGruposDisponiveis() {
		return gruposDisponiveis;
	}

	public void setGruposDisponiveis(List<Grupo> gruposDisponiveis) {
		this.gruposDisponiveis = gruposDisponiveis;
	}

	public Long[] getIdsGruposSelecionados() {
		return idsGruposSelecionados;
	}

	public void setIdsGruposSelecionados(Long[] idsGruposSelecionados) {
		this.idsGruposSelecionados = idsGruposSelecionados;
	}

	@PostConstruct
	public void init() {
		telefoneCategoriaValores = CategoriaTelefoneEnum.values();
		enderecoCategoriaValores = CategoriaEnderecoEnum.values();
		enderecoTipoValores = TipoEnderecoEnum.values();
		enderecoUfValores = UnidadeFederacaoEnum.values();
		
		UsuarioLogadoDTO usuarioDto = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
		
		gruposDisponiveis = daoGrupo.listarGruposPorUsuario(usuarioDto.getId());
	}
	
	public void incluir() {	
		try {
			validarCamposObrigatorios();
						
			validarPreenchimentoEmail();
			
			incluirObjeto();
		
			MensagensJSFUtils.adicionarMsgInfo("Contato incluído com sucesso", "");
		} catch (
				CamposObrigatoriosNaoInformadosException | 
				ConfirmacaoDeEmailInvalidaException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);			
		}
	}

	private void incluirObjeto() {
		anulaAsDAOs();
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new ContatoDAOImpl(em);		
		daoTelefone = new TelefoneDAOImpl(em);
		daoGrupo = new GrupoDAOImpl(em);
				
		try {		
			em.getTransaction().begin();
			
			// Cria/monta objetos e relacionamentos
			// 
			// Contato (apenas os atributos básicos) 
			Contato contato = criarObjContatoApartirDaView();
			contato.setDtInclusao(DataEHoraUtils.hoje());
			
			// Usuário logado
			Usuario usuarioLogadoBD = daoUsuario.buscarPor(AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId());
			
			if(usuarioLogadoBD != null)
				contato.setUsuario(usuarioLogadoBD);
			
			// Endereço
			Endereco endereco = criarObjEnderecoApartirDaView();
			
			if(endereco != null)
				contato.setEndereco(endereco);
			
			// Telefones
			List<Telefone> telefones = criarListaTelefonesApartirDaView(contato);			
			
			// Grupos
			List<Grupo> gruposSelecionados = criarListaGruposApartirDaView();
			
			if(gruposSelecionados != null)
				contato.setGrupos(gruposSelecionados);			
			
			// Persistência
			// 
			// - contato com usuário (e/ou endereço e/ou grupos)
			contato = dao.inserir(contato);
			
			// - telefones do contato
			if(telefones != null) {
				for (Iterator<Telefone> iterator = telefones.iterator(); iterator.hasNext();) {
					Telefone telefone = (Telefone) iterator.next();
					
					daoTelefone.inserir(telefone);				
				}
				
				contato.setTelefones(telefones);
			}
			
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
			if(em != null && em.isOpen())
				em.close();
		}
		
		inicializaDAOsValorPadrao();
	}

	private List<Grupo> criarListaGruposApartirDaView() {
		List<Grupo> gruposSelecionados = null;
		
		Long[] idsDosGrupos = this.idsGruposSelecionados;
		
		if(idsDosGrupos != null) {
			
			gruposSelecionados = new ArrayList<Grupo>(idsDosGrupos.length);
			
			for (int i = 0; i < idsDosGrupos.length; i++) {
									
				Grupo grupo = daoGrupo.buscarPor(idsDosGrupos[i]);
				
				gruposSelecionados.add(grupo);
			}				
		}
		
		return gruposSelecionados;
	}

	private List<Telefone> criarListaTelefonesApartirDaView(Contato contato) {
		List<Telefone> telefones = null;
		
		if(this.itensTelefoneListaDto != null) {
			for (Iterator<ItemListaTelefoneDTO> iterator = itensTelefoneListaDto.iterator(); iterator.hasNext();) {
				ItemListaTelefoneDTO telefoneDto = (ItemListaTelefoneDTO) iterator.next();
				
				Telefone novoTel = 
						new Telefone(
								telefoneDto.getCodigoArea(), 
								telefoneDto.getDdd(), 
								telefoneDto.getNumero(), 
								CategoriaTelefoneEnum.codigoIntParaEnum(telefoneDto.getCategoriaCodigo()));
				
				novoTel.setContato(contato);
				
				if(telefones == null)
					telefones = new ArrayList<Telefone>();
				
				telefones.add(novoTel);
			}
		}
		
		return telefones;
	}

	private Contato criarObjContatoApartirDaView() {
		Contato contato;
				
		contato = new Contato(nome, email, dtNascimento);
		
		return contato;
	}

	private Endereco criarObjEnderecoApartirDaView() {
		Endereco endereco = null;		
		
		if(enderecoTipoCodigo != null) {
			if(endereco == null)
				endereco = new Endereco();
			
			endereco.setTipo(TipoEnderecoEnum.codigoIntParaEnum(this.enderecoTipoCodigo));				
		}
		
		if(enderecoLogradouro != null) {
			if(endereco == null)
				endereco = new Endereco();
			
			endereco.setLogradouro(enderecoLogradouro);				
		}
		
		if(enderecoNumeroOuLoteamento != null) {
			if(endereco == null)
				endereco = new Endereco();
			
			endereco.setNumeroOuLoteamento(enderecoNumeroOuLoteamento);				
		}
		
		if(enderecoComplemento != null) {
			if(endereco == null)
				endereco = new Endereco();
			
			endereco.setComplemento(enderecoComplemento);				
		}
		
		if(enderecoLocalidade != null) {
			if(endereco == null)
				endereco = new Endereco();
			
			endereco.setLocalidade(enderecoLocalidade);				
		}
		
		if(enderecoUfCodigo != null) {
			if(endereco == null)
				endereco = new Endereco();
			
			endereco.setUf(UnidadeFederacaoEnum.codigoIntParaEnum(enderecoUfCodigo));				
		}
		
		if(enderecoCep != null) {
			if(endereco == null)
				endereco = new Endereco();
			
			// "apaga" o "-" da inserido pela mascara
			enderecoCep = enderecoCep.replaceAll("-", "");
			
			endereco.setCep(enderecoCep);				
		}
		
		if(enderecoCategoriaCodigo != null) {
			if(endereco == null)
				endereco = new Endereco();
			
			endereco.setCategoria(CategoriaEnderecoEnum.codigoIntParaEnum(enderecoCategoriaCodigo));				
		}
		
		return endereco;
	}

	private void validarPreenchimentoEmail() throws CamposObrigatoriosNaoInformadosException, ConfirmacaoDeEmailInvalidaException {
		if(!StringsUtils.ehStringVazia(email)) {
			if(StringsUtils.ehStringVazia(emailConfirmacao))
				throw new CamposObrigatoriosNaoInformadosException("Email (confirmação)");
			
			if(email.compareTo(emailConfirmacao) != 0)
				throw new ConfirmacaoDeEmailInvalidaException();
		}
	}

	private void validarCamposObrigatorios() throws CamposObrigatoriosNaoInformadosException {
		if(StringsUtils.ehStringVazia(nome))
			throw new CamposObrigatoriosNaoInformadosException("nome");
	}

	public void prepararAlteracao(Long idContatoPesquisa) {
		if(idContatoPesquisa != null) {
						
			Contato contato = dao.buscarPor(idContatoPesquisa);
			
			if(contato == null) {
				String msgErro = "Código/Id de contato inválido!"; 
				MensagensJSFUtils.adicionarMsgErro(msgErro, "");
			
				logger.error(msgErro);
				
				return;
			}
			
			this.id 				= idContatoPesquisa;
			this.nome 				= contato.getNome();
			this.email 				= contato.getEmail();
			this.emailConfirmacao 	= this.email;
			this.dtNascimento 		= contato.getDtNascimento();
			this.ehAlteracao 		= true;
						
			for (Telefone telCadastrado : contato.getTelefones()) {
				
				ItemListaTelefoneDTO dto = 
					new ItemListaTelefoneDTO(
							sequencia++,
							telCadastrado.getId(),
							telCadastrado.getCodigoArea(), 
							telCadastrado.getDdd(), 
							telCadastrado.getNumero(), 
							telCadastrado.getCategoria().getCodigo(),
							telCadastrado.getCategoria().getDescricao());
				
				this.getItensTelefoneListaDto().add(dto);
			}
			
			if(contato.getEndereco() != null) {
				if(contato.getEndereco().getCategoria() != null)
					this.enderecoCategoriaCodigo	= contato.getEndereco().getCategoria().getCodigo();
				
				this.enderecoCep 				= contato.getEndereco().getCep();
				this.enderecoComplemento 		= contato.getEndereco().getComplemento(); 
				this.enderecoLocalidade 		= contato.getEndereco().getLocalidade();
				this.enderecoLogradouro 		= contato.getEndereco().getLogradouro();
				this.enderecoNumeroOuLoteamento	= contato.getEndereco().getNumeroOuLoteamento();
				
				if(contato.getEndereco().getTipo() != null)
					this.enderecoTipoCodigo 		= contato.getEndereco().getTipo().getCodigo();
				
				if(contato.getEndereco().getUf() != null)
					this.enderecoUfCodigo			= contato.getEndereco().getUf().getCodigo();
			}
			
			if(contato.getGrupos() != null) {
				
				this.idsGruposSelecionados = new Long[contato.getGrupos().size()];
				
				int i = 0;
				
				for (Grupo grupo : contato.getGrupos()) {
					idsGruposSelecionados[i] = grupo.getId();
					
					i++;
				}
			}
		}
	}
	
	public void salvarAlteracao() {		
		try {
			validarCamposObrigatorios();
						
			validarPreenchimentoEmail();
			
			alterarObjeto();
		
			MensagensJSFUtils.adicionarMsgInfo("Contato alterado com sucesso", "");
		} catch (
				CamposObrigatoriosNaoInformadosException | 
				ConfirmacaoDeEmailInvalidaException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);			
		}
	}
	
	private void alterarObjeto() {
		anulaAsDAOs();
				
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new ContatoDAOImpl(em);
		daoTelefone = new TelefoneDAOImpl(em);
		daoGrupo = new GrupoDAOImpl(em);		
				
		try {		
			em.getTransaction().begin();
			
			Contato contato = dao.buscarPor(id);
			
			contato.setNome(nome);
			contato.setEmail(email);
			contato.setDtNascimento(dtNascimento);
			
			Endereco enderecoTela = criarObjEnderecoApartirDaView();
			
			if(enderecoTela != null) {
				if(contato.getEndereco() == null) {
					contato.setEndereco(enderecoTela);
				} else {
					contato.getEndereco().setCategoria(enderecoTela.getCategoria());
					contato.getEndereco().setCep(enderecoTela.getCep());
					contato.getEndereco().setComplemento(enderecoTela.getComplemento());
					contato.getEndereco().setLocalidade(enderecoTela.getLocalidade());
					contato.getEndereco().setLogradouro(enderecoTela.getLogradouro());
					contato.getEndereco().setNumeroOuLoteamento(enderecoTela.getNumeroOuLoteamento());
					contato.getEndereco().setTipo(enderecoTela.getTipo());
					contato.getEndereco().setUf(enderecoTela.getUf());
				}
			} else {
				contato.setEndereco(null);
			}

			if(this.idsGruposSelecionados != null) {
				List<Grupo> grupoSelecionados = new ArrayList<Grupo>(idsGruposSelecionados.length);
				
				for (int i = 0; i < idsGruposSelecionados.length; i++) {
					Long idGrupo = idsGruposSelecionados[i];
					
					Grupo grupo = daoGrupo.buscarPor(idGrupo);
					
					grupoSelecionados.add(grupo);
				}
				
				if(contato.getGrupos() != null) 
					contato.getGrupos().clear();
					
				contato.setGrupos(grupoSelecionados);
			} else {
				if(contato.getGrupos() != null)
					contato.getGrupos().clear();
			}
			
			contato = dao.alterar(contato);
			
			trataExclusaoTelefones(contato);
			
			trataInclusaoOuAlteracaoTelefones(contato);
			
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
			if(em != null && em.isOpen())
				em.close();
		}
		
		inicializaDAOsValorPadrao();
	}

	private void inicializaDAOsValorPadrao() {
		dao = new ContatoDAOImpl();
		daoTelefone = new TelefoneDAOImpl();
		daoGrupo = new GrupoDAOImpl();
	}

	private void anulaAsDAOs() {
		dao = null;
		daoTelefone = null;
		daoGrupo = null;
	}

	private void trataInclusaoOuAlteracaoTelefones(Contato contato) {
		List<ItemListaTelefoneDTO> telefonesInseridosOuAlterados = this.itensTelefoneListaDto;
		
		for (Iterator<ItemListaTelefoneDTO> iterator = telefonesInseridosOuAlterados.iterator(); iterator.hasNext();) {
			ItemListaTelefoneDTO itemDto = (ItemListaTelefoneDTO) iterator.next();
			
			if(itemDto.getEhInserir()) {
				Telefone telNovo = new Telefone(
						itemDto.getCodigoArea(), 
						itemDto.getDdd(),
						itemDto.getNumero(),
						CategoriaTelefoneEnum.codigoIntParaEnum(itemDto.getCategoriaCodigo()));
				
				telNovo.setContato(contato);
				
				if(contato.getTelefones() == null)
					contato.setTelefones(new ArrayList<Telefone>());
				
				contato.getTelefones().add(telNovo);
				
				daoTelefone.inserir(telNovo);
			} else if(itemDto.getEhAlterar()) {
				boolean achou = false;
				
				List<Telefone> telefones = contato.getTelefones();
				
				for (Iterator<Telefone> iterator2 = telefones.iterator(); iterator2.hasNext() && !achou;) {
					Telefone telefone = (Telefone) iterator2.next();
					
					if(telefone.getId().longValue() == itemDto.getIdTelefone().longValue()) {
						achou = true;
						
						telefone.setCodigoArea(itemDto.getCodigoArea());
						telefone.setDdd(itemDto.getDdd());
						telefone.setNumero(itemDto.getNumero());
						telefone.setCategoria(CategoriaTelefoneEnum.codigoIntParaEnum(itemDto.getCategoriaCodigo()));
						
						daoTelefone.alterar(telefone);
					}
				}
			}
		}
	}

	private void trataExclusaoTelefones(Contato contato) {
		List<Telefone> telefonesBD = contato.getTelefones();
		
		List<ItemListaTelefoneDTO> telefonesAExcluir = this.itensTelefoneListaDtoExcluidos;
		
		if(telefonesAExcluir != null && telefonesAExcluir.size() > 0) {
			for (Iterator<ItemListaTelefoneDTO> iterator = telefonesAExcluir.iterator(); iterator.hasNext();) {
				ItemListaTelefoneDTO itemDto = (ItemListaTelefoneDTO) iterator.next();
				
				for (Iterator<Telefone> iterator2 = telefonesBD.iterator(); iterator2.hasNext();) {
					Telefone telefoneBD = (Telefone) iterator2.next();
					
					if(itemDto.getIdTelefone().longValue() == telefoneBD.getId().longValue()) {
						iterator2.remove();
						daoTelefone.deletar(telefoneBD);
					}
				}	
			}							
		}
	}
	
	public void incluirTelefone() {
		
		try {
			validarCamposObrigatoriosTelefone();
					
			CategoriaTelefoneEnum categoriaTelefone = CategoriaTelefoneEnum.codigoIntParaEnum(telefoneCategoriaCodigo.intValue()); 
					
			ItemListaTelefoneDTO itemTelDto = 
					new ItemListaTelefoneDTO(
							sequencia ++, 
							null,
							telefoneCodigoArea, 
							telefoneDdd, 
							telefoneNumero, 
							telefoneCategoriaCodigo.intValue(),
							categoriaTelefone.getDescricao());
			
			itemTelDto.setEhInserir(true);
			itemTelDto.setEhAlterar(false);
			
			this.getItensTelefoneListaDto().add(itemTelDto);
			
			this.telefoneCodigoArea = null;
			this.telefoneDdd = null;
			this.telefoneNumero = null;
			this.telefoneCategoriaCodigo = null;
		} catch (
				CamposObrigatoriosNaoInformadosException | 
				TamanhoCampoInvalidoException | 
				CategoriaTelefoneEnumInvalidaException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);			
		}		
	}

	private void validarCamposObrigatoriosTelefone() throws CamposObrigatoriosNaoInformadosException,
			TamanhoCampoInvalidoException, CategoriaTelefoneEnumInvalidaException {
		if(telefoneNumero == null)			
			throw new CamposObrigatoriosNaoInformadosException("telefones/número");
		
		if(String.valueOf(telefoneNumero).length() < 8)
			throw new TamanhoCampoInvalidoException("Telefones/número", 8, 0);
		
		if(String.valueOf(telefoneNumero).length() > 9)
			throw new TamanhoCampoInvalidoException("Telefones/número", 0, 9);
		
		if(telefoneCategoriaCodigo == null)
			throw new CategoriaTelefoneEnumInvalidaException();
		
		CategoriaTelefoneEnum categoriaTelefone = CategoriaTelefoneEnum.codigoIntParaEnum(telefoneCategoriaCodigo.intValue());
		
		if(categoriaTelefone == null)
			throw new CategoriaTelefoneEnumInvalidaException();
	}
	
	public void alterarTelefone() {
		try {
			validarCamposObrigatoriosTelefone();
					
			CategoriaTelefoneEnum categoriaTelefone = CategoriaTelefoneEnum.codigoIntParaEnum(telefoneCategoriaCodigo.intValue()); 
			
			ItemListaTelefoneDTO dto = obterItemListaTelefoneDtoAPartirTelefoneIndex();
			
			if(dto != null) {
				dto.setEhInserir(false);
				dto.setEhAlterar(true);
				
				dto.setCodigoArea(telefoneCodigoArea);
				dto.setDdd(telefoneDdd);
				dto.setNumero(telefoneNumero);
				dto.setCategoriaCodigo(telefoneCategoriaCodigo);
				dto.setCategoriaDescricao(categoriaTelefone.getDescricao());
			}
			
			this.telefoneCodigoArea = null;
			this.telefoneDdd = null;
			this.telefoneNumero = null;
			this.telefoneCategoriaCodigo = null;
			
			this.telefoneIndex = null;
		} catch (
				CamposObrigatoriosNaoInformadosException | 
				TamanhoCampoInvalidoException | 
				CategoriaTelefoneEnumInvalidaException e) {
			
			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);			
		}	
	}
	
	public void carregarTelefoneParaAlteracao(Integer sequencia) throws IdTelefoneInvalidoException {
		this.telefoneIndex = sequencia;
		
		ItemListaTelefoneDTO dto = obterItemListaTelefoneDtoAPartirTelefoneIndex();
		
		if(dto != null) {
			telefoneCodigoArea = dto.getCodigoArea();
			telefoneDdd = dto.getDdd();
			telefoneNumero = dto.getNumero();
			telefoneCategoriaCodigo = dto.getCategoriaCodigo();
		} else
			throw new IdTelefoneInvalidoException();
	}

	private ItemListaTelefoneDTO obterItemListaTelefoneDtoAPartirTelefoneIndex() {
		ItemListaTelefoneDTO dto = null;
		
		for (Iterator<ItemListaTelefoneDTO> iterator = itensTelefoneListaDto.iterator(); 
				iterator.hasNext() && dto == null;
				) {
			ItemListaTelefoneDTO itemLista = (ItemListaTelefoneDTO) iterator.next();
			
			if(itemLista.getSequencia().intValue() == this.telefoneIndex.intValue())
				dto = itemLista; 
		}
		return dto;
	}
	
	public void excluirTelefone(Integer sequencia) throws IdTelefoneInvalidoException {
		this.telefoneIndex = sequencia;
		
		ItemListaTelefoneDTO dto = obterItemListaTelefoneDtoAPartirTelefoneIndex();
		
		if(dto == null)
			throw new IdTelefoneInvalidoException();
		
		dto.setEhInserir(false);
		dto.setEhAlterar(false);
		
		this.getItensTelefoneListaDto().remove(dto);
		
		if(this.ehAlteracao)
			this.getItensTelefoneListaDtoExcluidos().add(dto);
	
		this.telefoneIndex = null;
	}

	public List<ItemListaTelefoneDTO> getItensTelefoneListaDtoExcluidos() {
		if(itensTelefoneListaDtoExcluidos == null)
			this.setItensTelefoneListaDtoExcluidos(new ArrayList<ItemListaTelefoneDTO>());
		
		return itensTelefoneListaDtoExcluidos;
	}

	public void setItensTelefoneListaDtoExcluidos(List<ItemListaTelefoneDTO> itensTelefoneListaDtoExcluidos) {
		this.itensTelefoneListaDtoExcluidos = itensTelefoneListaDtoExcluidos;
	}
	
	
}
