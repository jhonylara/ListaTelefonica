package br.edu.aplicacao.listeners;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.persistencia.testes.InicializadorBDEmMemoria;

public class GestorLogadoListener implements Filter {
	
	public static String USUARIO_LOGADO = "USUARIO_LOGADO";
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void init(FilterConfig arg0) throws ServletException {
		logger.info("init GestorLogadoListener");
		
		// TODO: deve ser excluída esta referência quando for utilizar uma BD
		//	real, por exemplo, MySQL, Oracle,...
		try {
			new InicializadorBDEmMemoria();
		} catch (Exception e) {
			logger.error("ERRO ao criar BD em memória", e);
		}
	}
	
	public void destroy() {
		logger.info("destroy GestorLogadoListener");		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = obterELogarHttpServletRequest(request);
		
		HttpSession session = obterELogarHttpSession(req);

		logarURI(req);
		
		if (temUsuarioLogadoNaSessao(session)				
				|| estahAcessandoPaginaLogin(req)				
				|| estahBuscandoRecursoJSF(req)				
				|| estahBuscandoRecursoAplicacao(req)
			) {
			
			chain.doFilter(request, response);
		} else {
			logarAcessoNaoAutorizado();
			redirecionaParaLogin(response);
		}
	}

	private boolean estahBuscandoRecursoAplicacao(HttpServletRequest req) {
		return req.getRequestURI().contains("resources/");
	}

	private boolean estahBuscandoRecursoJSF(HttpServletRequest req) {
		return req.getRequestURI().contains("javax.faces.resource/");
	}

	private boolean estahAcessandoPaginaLogin(HttpServletRequest req) {
		return req.getRequestURI().endsWith("login.jsf");
	}

	private boolean temUsuarioLogadoNaSessao(HttpSession session) {
		return session.getAttribute( USUARIO_LOGADO ) != null;
	}

	private void redirecionaParaLogin(ServletResponse response) throws IOException {
		redireciona("/aplicacao/pages/login.jsf", response);
	}

	private void logarAcessoNaoAutorizado() {
		logger.info("USUARIO NÃO LOGADO E/OU ACESSO NÃU AUTORIZADO");
	}

	private void logarURI(HttpServletRequest req) {
		if(req != null && req.getRequestURI() != null)
			logger.info("req.getRequestURI() = " + req.getRequestURI());
		else
			logger.info("req.getRequestURI() = null ");
	}

	private HttpSession obterELogarHttpSession(HttpServletRequest req) {
		HttpSession session = req.getSession();		
		if(session != null)
			logger.info("HttpSession");
		else
			logger.info("HttpSession=null");
		return session;
	}

	private HttpServletRequest obterELogarHttpServletRequest(ServletRequest request) {
		HttpServletRequest req = (HttpServletRequest) request;
		if(req != null)
			logger.info("HttpServletRequest");
		else
			logger.info("HttpServletRequest=null");
		return req;
	}
	
	private void redireciona(String url, ServletResponse response)
			throws IOException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.sendRedirect(url);
	}
}
