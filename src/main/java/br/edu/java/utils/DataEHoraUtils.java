package br.edu.java.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DataEHoraUtils {

	/**
	 * Criar uma data (date) a partir de uma string com data/hora - padrão brasileiro dd/mm/aaaa [hh:mm].
	 * 
	 * @param dataHoraString 
	 * @return Date
	 * @throws ParseException 
	 */
	public static Date dataHoraStringParaDate(String dataHoraStringPtBr) throws ParseException {
		// ex.: 01/01/1980 22:00 ou 28/03/2001 11:30
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date dataEHora = formato.parse(dataHoraStringPtBr);
		return dataEHora;
	}

	/**
	 * Retornar a data/hora atual.
	 * 
	 * @return Date
	 */
	public static Date hoje() {
		
		return Calendar.getInstance().getTime();
	}

	public static LocalDate converteDateParaLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
