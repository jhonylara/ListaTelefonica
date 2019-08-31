package br.edu.aplicacao.dtos;

import java.time.LocalDate;

public class HistoricoInclusaoDTO {

	private LocalDate dataLocal;
	private String mesAnoDesc;
	private Integer qtd;
	
	public HistoricoInclusaoDTO() {		
	}	
	public HistoricoInclusaoDTO(LocalDate dataLocal, String mesAnoDesc, Integer qtd) {
		this.dataLocal = dataLocal;
		this.mesAnoDesc = mesAnoDesc;
		this.qtd = qtd;
	}	
	public LocalDate getDataLocal() {
		return dataLocal;
	}
	public void setDataLocal(LocalDate dataLocal) {
		this.dataLocal = dataLocal;
	}
	public String getMesAnoDesc() {
		return mesAnoDesc;
	}
	public void setMesAnoDesc(String mesAnoDesc) {
		this.mesAnoDesc = mesAnoDesc;
	}
	public Integer getQtd() {
		return qtd;
	}
	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}	
}
