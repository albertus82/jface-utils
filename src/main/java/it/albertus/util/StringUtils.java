package it.albertus.util;

public class StringUtils {

	public static boolean isNotEmpty(final String str) {
		return str != null && str.length() != 0;
	}

	public static boolean isNotBlank(final String str) {
		return str != null && str.trim().length() != 0;
	}

	public static String trimToEmpty(final String str) {
		return str != null ? str.trim() : "";
	}

	public static boolean isNumeric(final String str) {
		if (str == null) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static String substringAfter(final String str, final String separator) {
		if (str == null || str.length() == 0) {
			return str;
		}
		if (separator == null || str.indexOf(separator) == -1) {
			return "";
		}
		return str.substring(str.indexOf(separator) + separator.length());
	}

	public static String substringBefore(final String str, final String separator) {
		if (str == null || str.length() == 0) {
			return str;
		}
		if (separator == null || str.indexOf(separator) == -1) {
			return str;
		}
		return str.substring(0, str.indexOf(separator));
	}

}
