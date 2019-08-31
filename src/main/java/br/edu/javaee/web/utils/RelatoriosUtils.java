package br.edu.javaee.web.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RelatoriosUtils {
	
	public static void executarERealizarDownloadPDF(Map<String, Object> params, String caminhoArquivoCompilado, List<?> dadosListaBeans, String nomeArquivoDownload) throws IOException, JRException {
		String caminhoFisico = JSFUtils.getFacesContext().getExternalContext().getRealPath(caminhoArquivoCompilado);
				
		File file = new File(caminhoFisico);
		
		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(dadosListaBeans, false);
		
		JasperPrint print = JasperFillManager.fillReport(file.getPath(), params, source);
		
		HttpServletResponse response = (HttpServletResponse) JSFUtils.getFacesContext().getExternalContext().getResponse();
		
		response.addHeader("Content-disposition", "attachment;filename=" + nomeArquivoDownload);
		
		ServletOutputStream stream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(print, stream);
		
		JSFUtils.getFacesContext().responseComplete();
	}

}
