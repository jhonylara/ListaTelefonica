package br.edu.javaee.web.utils;

import javax.faces.application.FacesMessage;

import org.slf4j.Logger;

public class MensagensJSFUtils {

	public static void adicionarMsgErro(String mensagem, String detalhe) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, detalhe);
        JSFUtils.getFacesContext().addMessage(null, msg);
	}
	
	public static void adicionarMsgInfo(String mensagem, String detalhe) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, detalhe);
        JSFUtils.getFacesContext().addMessage(null, msg);
	}
	
	public static void adicionarMsgWarn(String mensagem, String detalhe) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, detalhe);
        JSFUtils.getFacesContext().addMessage(null, msg);
	}
	
	public static void msgELogDeERROInternoEOuSistema(Logger logger, Exception e) {
		String msgErro = "ERRO interno e/ou de sistema: contate o administrador!"; 
		
		MensagensJSFUtils.adicionarMsgErro(msgErro, "");
		
		logger.error(msgErro, e);
	}	
}
