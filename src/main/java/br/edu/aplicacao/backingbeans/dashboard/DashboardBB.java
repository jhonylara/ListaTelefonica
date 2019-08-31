package br.edu.aplicacao.backingbeans.dashboard;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.HistoricoInclusaoDTO;
import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.persistencia.interfaces.IContatoDAO;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.ContatoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.java.utils.DataEHoraUtils;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@RequestScoped
public class DashboardBB implements Serializable {

	private static final long serialVersionUID = 1L;	
	private static final int QTD_MESES = 6;
	
	private IContatoDAO daoContato = new ContatoDAOImpl();	
	private IUsuarioDAO daoUsuario = new UsuarioDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private LineChartModel graficoDeLinhasHistInclusao;
	
	private PieChartModel graficoDePizzaContatosDdd;
	
	private BarChartModel graficoDeBarraContatosLocalidade;
	
	private Long qtdTotalDeUsuarios;	
	private Long qtdTotalDeContatos;
		
	@PostConstruct
	private void init() {
		
		try {
			atualizarTotais();
			
			criarOuAtualizarGraficoHisInclusao();
			
			criarOuAtualizarGraficoDePizzaContatosDdd();
			
			criarOuAtualizarGraficoDeBarraContatosLocalidade();
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}
	}
	
	private void atualizarTotais() {
		qtdTotalDeUsuarios = daoUsuario.qtdTotal();		
		
		qtdTotalDeContatos = daoContato.qtdTotalPorUsuario(AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId());
	}

	public LineChartModel getGraficoDeLinhasHistInclusao() {
		return graficoDeLinhasHistInclusao;
	}

	public void setGraficoDeLinhasHistInclusao(LineChartModel graficoDeLinhasHistInclusao) {
		this.graficoDeLinhasHistInclusao = graficoDeLinhasHistInclusao;
	}

	public PieChartModel getGraficoDePizzaContatosDdd() {
		return graficoDePizzaContatosDdd;
	}

	public void setGraficoDePizzaContatosDdd(PieChartModel graficoDePizzaContatosDdd) {
		this.graficoDePizzaContatosDdd = graficoDePizzaContatosDdd;
	}

	public BarChartModel getGraficoDeBarraContatosLocalidade() {
		return graficoDeBarraContatosLocalidade;
	}

	public void setGraficoDeBarraContatosLocalidade(BarChartModel graficoDeBarraContatosLocalidade) {
		this.graficoDeBarraContatosLocalidade = graficoDeBarraContatosLocalidade;
	}

	public Long getQtdTotalDeUsuarios() {
		return qtdTotalDeUsuarios;
	}

	public void setQtdTotalDeUsuarios(Long qtdTotalDeUsuarios) {
		this.qtdTotalDeUsuarios = qtdTotalDeUsuarios;
	}

	public Long getQtdTotalDeContatos() {
		return qtdTotalDeContatos;
	}

	public void setQtdTotalDeContatos(Long qtdTotalDeContatos) {
		this.qtdTotalDeContatos = qtdTotalDeContatos;
	}

	private void criarOuAtualizarGraficoDeBarraContatosLocalidade() {
		graficoDeBarraContatosLocalidade = new BarChartModel();

		Object qtdContatosPorLocalidade = daoContato.qtdContatosPorLocalidade( AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId() );
		
		ChartSeries[] series = null;
		
		Long max = Long.MIN_VALUE;
		
		if(qtdContatosPorLocalidade != null && qtdContatosPorLocalidade instanceof List) {
			
			List<Object> listaAgrupamentos = ((List<Object>)qtdContatosPorLocalidade);
			
			series = new ChartSeries[listaAgrupamentos.size()];
			
			int indiceSeries = 0;
			
			for (Iterator iterator = listaAgrupamentos.iterator(); iterator.hasNext();) {
				Object[] agrupamento = (Object[]) iterator.next();
				
				Object localidade = agrupamento[0];
				String strLocalidade = "";
				if(localidade == null)
					strLocalidade = "Não informada/Sem endereço";
				else
					strLocalidade = "" + localidade;
				
				Object qtdLocalidade = agrupamento[1];
				Long valorQtdLocalidade = 0L;
				if(qtdLocalidade != null)
					valorQtdLocalidade = (Long)qtdLocalidade; 

				if(valorQtdLocalidade >= max)
					max = valorQtdLocalidade;
				
				series[indiceSeries] = new ChartSeries();
				series[indiceSeries].setLabel(strLocalidade);
				series[indiceSeries].set("Localidade", valorQtdLocalidade);
				
				indiceSeries++;
			}			
		}

		if(series != null) {
			for (int i = 0; i < series.length; i++) {
				
				graficoDeBarraContatosLocalidade.addSeries(series[i]);		
			}			
		}

		graficoDeBarraContatosLocalidade.setTitle("Contatos* por Localidade");
		graficoDeBarraContatosLocalidade.setLegendPosition("ne");
		
		graficoDeBarraContatosLocalidade.setShowPointLabels(true);

		Axis xAxis = graficoDeBarraContatosLocalidade.getAxis(AxisType.X);
		xAxis.setLabel("");

		Axis yAxis = graficoDeBarraContatosLocalidade.getAxis(AxisType.Y);
		yAxis.setLabel("Qtd. de Contatos");
		yAxis.setMin(0);
		yAxis.setMax(max + 1);		
	}

	private void criarOuAtualizarGraficoDePizzaContatosDdd() {
		graficoDePizzaContatosDdd = new PieChartModel();
        				
		Object qtdContatosPorDdd = daoContato.qtdContatosPorDdd( AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId() );
		
		if(qtdContatosPorDdd != null && qtdContatosPorDdd instanceof List) {
			for (Iterator iterator = ((List<Object>)qtdContatosPorDdd).iterator(); iterator.hasNext();) {
				Object[] agrupamento = (Object[]) iterator.next();
				
				Object ddd = agrupamento[0];
				String strDdd = "";
				if(ddd == null)
					strDdd = "Não informado/Sem telefone";
				else
					strDdd = "" + ddd;
				
				Object qtdDdd = agrupamento[1];
				Long valorQtdDdd = 0L;
				if(qtdDdd != null)
					valorQtdDdd = (Long)qtdDdd; 
				
				graficoDePizzaContatosDdd.set(strDdd, valorQtdDdd);		
			}			
		}
         
        graficoDePizzaContatosDdd.setTitle("%Telefones/Contatos* por DDD");
        graficoDePizzaContatosDdd.setLegendPosition("e");
        graficoDePizzaContatosDdd.setFill(false);
        graficoDePizzaContatosDdd.setShowDataLabels(true);
        graficoDePizzaContatosDdd.setDiameter(200);
	}

	private void criarOuAtualizarGraficoHisInclusao() {
		graficoDeLinhasHistInclusao = new LineChartModel();				
		
		ChartSeries serieIncUsuarios = new ChartSeries();
		serieIncUsuarios.setLabel("Usuários");
		
		ChartSeries serieIncContatos = new ChartSeries();
		serieIncContatos.setLabel("Contatos");
		
		List<HistoricoInclusaoDTO> histIncUsuarios = this.historicoInclusaoUltimo6MesesUsuarios();
		List<HistoricoInclusaoDTO> histIncContatos = this.historicoInclusaoUltimo6MesesContatos();
		
		Integer max = Integer.MIN_VALUE;
		
		for (Iterator<HistoricoInclusaoDTO> iterator = histIncUsuarios.iterator(); iterator.hasNext();) {
			HistoricoInclusaoDTO dtoUsr = (HistoricoInclusaoDTO) iterator.next();
		
			serieIncUsuarios.set( dtoUsr.getMesAnoDesc() , dtoUsr.getQtd());			
					
			if(dtoUsr.getQtd() >= max)
				max = dtoUsr.getQtd();			
		}
		
		for (Iterator<HistoricoInclusaoDTO> iterator = histIncContatos.iterator(); iterator.hasNext();) {
			HistoricoInclusaoDTO dtoContato = (HistoricoInclusaoDTO) iterator.next();
		
			serieIncContatos.set( dtoContato.getMesAnoDesc() , dtoContato.getQtd());			
					
			if(dtoContato.getQtd() >= max)
				max = dtoContato.getQtd();			
		}
		
        graficoDeLinhasHistInclusao.addSeries(serieIncUsuarios);
        graficoDeLinhasHistInclusao.addSeries(serieIncContatos);
 
		graficoDeLinhasHistInclusao.setTitle("Usuários e Contatos* - Histórico de inclusão dos últimos 6 meses");
		graficoDeLinhasHistInclusao.setLegendPosition("e");
		graficoDeLinhasHistInclusao.setShowPointLabels(true);
		graficoDeLinhasHistInclusao.getAxes().put(AxisType.X, new CategoryAxis("Mês/Ano"));
		
		Axis yAxis = graficoDeLinhasHistInclusao.getAxis(AxisType.Y);
        yAxis.setLabel("Qtd. de Inclusões");
        yAxis.setMin(0);
        yAxis.setMax(max + 1);
	}

	private List<HistoricoInclusaoDTO> historicoInclusaoUltimo6MesesContatos() {
		List<HistoricoInclusaoDTO> historico = criarListaZeradaHistoricoInclusaoDTO();
		
		List<Contato> todosContatos = daoContato.listarContatosPorUsuario( AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId() );
		
		if(todosContatos != null && todosContatos.size() > 0) {
			for (Iterator<Contato> iterator2 = todosContatos.iterator(); iterator2.hasNext();) {
				Contato contato = (Contato) iterator2.next();

				boolean contouContato = false;
				
				for (Iterator<HistoricoInclusaoDTO> iterator1 = historico.iterator(); iterator1.hasNext() && !contouContato;) {
					HistoricoInclusaoDTO dtoCalculo = (HistoricoInclusaoDTO) iterator1.next();			
			
					Date dateDtInclusao = contato.getDtInclusao();
					
					LocalDate ldDtInclusao = DataEHoraUtils.converteDateParaLocalDate(dateDtInclusao);
					
					if(dtoCalculo.getDataLocal().getYear() == ldDtInclusao.getYear()
							&& dtoCalculo.getDataLocal().getMonth() == ldDtInclusao.getMonth()) {
						dtoCalculo.setQtd( dtoCalculo.getQtd() + 1 );
						contouContato = true;
					}
				}				
			}
		}
				
		return historico;
	}
	
	private List<HistoricoInclusaoDTO> historicoInclusaoUltimo6MesesUsuarios() {
		List<HistoricoInclusaoDTO> historico = criarListaZeradaHistoricoInclusaoDTO();
		
		List<Usuario> todosUsuarios = daoUsuario.listarTodos();
		
		if(todosUsuarios != null && todosUsuarios.size() > 0) {
			for (Iterator<Usuario> iterator2 = todosUsuarios.iterator(); iterator2.hasNext();) {
				Usuario usuario = (Usuario) iterator2.next();

				boolean contouUsuario = false;
				
				for (Iterator<HistoricoInclusaoDTO> iterator1 = historico.iterator(); iterator1.hasNext() && !contouUsuario;) {
					HistoricoInclusaoDTO dtoCalculo = (HistoricoInclusaoDTO) iterator1.next();			
			
					Date dateDtInclusao = usuario.getDtInclusao();
					
					LocalDate ldDtInclusao = DataEHoraUtils.converteDateParaLocalDate(dateDtInclusao);
					
					if(dtoCalculo.getDataLocal().getYear() == ldDtInclusao.getYear()
							&& dtoCalculo.getDataLocal().getMonth() == ldDtInclusao.getMonth()) {
						dtoCalculo.setQtd( dtoCalculo.getQtd() + 1 );
						contouUsuario = true;
					}
				}				
			}
		}
		
		
		return historico;
	}

	private List<HistoricoInclusaoDTO> criarListaZeradaHistoricoInclusaoDTO() {
								
		List<HistoricoInclusaoDTO> historico = new ArrayList<HistoricoInclusaoDTO>(QTD_MESES);
	
		LocalDate dataLocal = LocalDate.now();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yy");
						
		HistoricoInclusaoDTO dtoPreparacao;		
		for (int i = (QTD_MESES-1); i > 0; i--) {
			dtoPreparacao = new HistoricoInclusaoDTO();
			
			dtoPreparacao.setDataLocal(dataLocal.minusMonths(i));	
			dtoPreparacao.setMesAnoDesc( dtoPreparacao.getDataLocal().format(dtf) );		
			dtoPreparacao.setQtd( 0 );
			
			historico.add(dtoPreparacao);
		}
		dtoPreparacao = new HistoricoInclusaoDTO();		
		dtoPreparacao.setDataLocal(dataLocal);			
		dtoPreparacao.setMesAnoDesc( dtoPreparacao.getDataLocal().format(dtf) );			
		dtoPreparacao.setQtd( 0 );		
		historico.add(dtoPreparacao);
		
		return historico;
	}	
}
