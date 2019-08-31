package br.edu.javaee.web.utils;

import java.util.logging.Logger;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Classe que informa as fases (ou ciclo de vida) dos componentes JSF
 * 
 * @author vagnercml
 */
public class CicloDeVidaJSFListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(this.getClass().getName());

	public void beforePhase(PhaseEvent event) {
		log.info("$$$$ INICIO DA FASE: " + event.getPhaseId());
	}

	public void afterPhase(PhaseEvent event) {
		log.info("---- " + event.getPhaseId());
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
