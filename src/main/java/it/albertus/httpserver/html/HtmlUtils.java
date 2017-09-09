package it.albertus.httpserver.html;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HtmlUtils {

	private static final Set<Character> charsToEscapeForEcmaScript = new HashSet<Character>(Arrays.asList('&', '"', '<', '>', '\'', '/'));

	private HtmlUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static String escapeEcmaScript(final String unescaped) {
		String escaped = unescaped.replace("\\", "\\\\");
		final Set<Character> replacedChars = new HashSet<Character>();
		for (final char c : escaped.toCharArray()) {
			if (!replacedChars.contains(c) && (c < 0x20 || c > 0x7F || charsToEscapeForEcmaScript.contains(c))) {
				escaped = escaped.replace(Character.toString(c), String.format(c > 0xFF ? "\\u%04X" : "\\x%02X", (int) c));
				replacedChars.add(c);
			}
		}
		return escaped;
	}

	public static String escapeHtml(final String unescaped) {
		return unescaped.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;").replace("'", String.format("&#%d;", (int) '\''));
	}

	public static String unescapeHtml(final String escaped) {
		return escaped.replace(String.format("&#%d;", (int) '\''), "'").replace("&gt;", ">").replace("&lt;", "<").replace("&quot;", "\"").replace("&amp;", "&");
	}

}
