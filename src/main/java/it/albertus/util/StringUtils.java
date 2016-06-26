package it.albertus.util;

public class StringUtils {

	public static boolean isNotBlank(final String str) {
		return str != null && str.trim().length() != 0;
	}

	public static String trimToEmpty(final String str) {
		return str != null ? str.trim() : "";
	}

}
