package br.edu.java.utils;

public class StringsUtils {

	public static boolean ehStringVazia(String str) {
		return str == null || (str != null && "".equalsIgnoreCase(str));
	}
}
