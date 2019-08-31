package br.edu.aplicacao.backingbeans.relatorios;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.CodigoDescricaoDTO;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.persistencia.interfaces.IContatoDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.ContatoDAOImpl;
import br.edu.javaee.web.utils.JSFUtils;
import br.edu.javaee.web.utils.MensagensJSFUtils;
import br.edu.javaee.web.utils.RelatoriosUtils;

@Named
@ViewScoped
public class GerarListaDeAniversariantesBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String NOME_PARAM_MES = "MES";
	
	private static final String NOME_PARAM_CAMINHO_LOGO = "CAMINHO_LOGO";
	
	private IContatoDAO dao = new ContatoDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Long mesCodigo;
	
	private List<CodigoDescricaoDTO> mesesDtos;
	
	@PostConstruct
	private void init() {
		LocalDate hoje = LocalDate.now();
		
		mesCodigo = new Long(hoje.getMonthValue());
		
		mesesDtos = new ArrayList<CodigoDescricaoDTO>(12);
		
		mesesDtos.add(new CodigoDescricaoDTO(1L, "Janeiro"));
		mesesDtos.add(new CodigoDescricaoDTO(2L, "Fevereiro"));
		mesesDtos.add(new CodigoDescricaoDTO(3L, "Março"));
		mesesDtos.add(new CodigoDescricaoDTO(4L, "Abril"));
		mesesDtos.add(new CodigoDescricaoDTO(5L, "Maio"));
		mesesDtos.add(new CodigoDescricaoDTO(6L, "Junho"));
		mesesDtos.add(new CodigoDescricaoDTO(7L, "Julho"));
		mesesDtos.add(new CodigoDescricaoDTO(8L, "Agosto"));
		mesesDtos.add(new CodigoDescricaoDTO(9L, "Setembro"));
		mesesDtos.add(new CodigoDescricaoDTO(10L, "Outubro"));
		mesesDtos.add(new CodigoDescricaoDTO(11L, "Novembro"));
		mesesDtos.add(new CodigoDescricaoDTO(12L, "Dezembro"));
	}

	public Long getMesCodigo() {
		return mesCodigo;
	}

	public void setMesCodigo(Long mesCodigo) {
		this.mesCodigo = mesCodigo;
	}

	public List<CodigoDescricaoDTO> getMesesDtos() {
		return mesesDtos;
	}

	public void setMesesDtos(List<CodigoDescricaoDTO> mesesDtos) {
		this.mesesDtos = mesesDtos;
	}

	public void gerarPDF() {
		
		try {
			List<Contato> contatos = selecionarAniversariantes();
			
			if(contatos == null || (contatos != null && contatos.size() <= 0)) {
				MensagensJSFUtils.adicionarMsgInfo("Nenhum contato selecionado.", "");
			} else {
				String mesDescricao = obterNomeDoMesSelecionado();
				
				String nomeArquivoPDF = "Rel_ListaDeAniversariantes_" + mesDescricao + ".pdf";
				
				String caminhoArquivoCompilado = "/relatorios/rel_aniversariantes.jasper";

				String caminhoENomeLogo = JSFUtils.getFacesContext().getExternalContext().getRealPath("/resources/images/jsf_javaee.png");
				
				Map<String, Object> parametros = new HashMap<String, Object>();
				
				parametros.put(NOME_PARAM_MES, mesDescricao);
				parametros.put(NOME_PARAM_CAMINHO_LOGO, caminhoENomeLogo);
				
				RelatoriosUtils.executarERealizarDownloadPDF(parametros, caminhoArquivoCompilado, contatos, nomeArquivoPDF);				
			}
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}	
	}

	private String obterNomeDoMesSelecionado() {
		return ((CodigoDescricaoDTO)mesesDtos.get(mesCodigo.intValue()-1)).getDescricao();
	}

	private List<Contato> selecionarAniversariantes() {
		UsuarioLogadoDTO usuarioLogado = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
		
		List<Contato> contatos = dao.listarContatosPorUsuario(usuarioLogado.getId());
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		
		if(contatos != null) {
			for (Iterator<Contato> iterator = contatos.iterator(); iterator.hasNext();) {
				Contato contato = (Contato) iterator.next();
				
				if(contato.getDtNascimento() == null)
					iterator.remove();
				else {
					String strMes = sdf.format(contato.getDtNascimento());
					String strMesSelecionado = "" + mesCodigo;
					if(mesCodigo < 10)
						strMesSelecionado = "0" + strMesSelecionado; 
					 
					if(!strMes.equalsIgnoreCase(strMesSelecionado))
						iterator.remove();
				}						
			}
		}
		return contatos;
	}
}
