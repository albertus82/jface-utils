package it.albertus.util;

public enum NewLine {

	CR('\r'),
	LF('\n'),
	CRLF('\r', '\n');

	public static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");

	private final char[] characters;
	private final String string;

	private NewLine(char... characters) {
		this.characters = characters;
		this.string = String.valueOf(characters);
	}

	/**
	 * Restituisce un array di caratteri contenente la sequenza di caratteri che
	 * rappresenta il ritorno a capo.
	 * 
	 * @return l'array di caratteri che rappresenta il ritorno a capo.
	 */
	public char[] toCharArray() {
		return characters;
	}

	/**
	 * Restituisce una stringa contenente la sequenza di caratteri che
	 * rappresenta il ritorno a capo.
	 * 
	 * @return la stringa che rappresenta il ritorno a capo.
	 */
	public String toString() {
		return string;
	}

	/**
	 * Restituisce l'<b>enum</b> corrispondente al tipo di ritorno a capo
	 * desiderato.
	 * 
	 * @param characters
	 *            una stringa tra "CR", "\r" (Macintosh), "LF", "\n" (Unix),
	 *            "CRLF", "\r\n" (DOS/Windows).
	 * @return l'<b>enum</b> corrispondente al tipo di ritorno a capo passato
	 *         come parametro, oppure null se non riconosciuto.
	 */
	public static NewLine getEnum(String characters) {
		if (characters != null && characters.length() != 0) {
			for (NewLine newLine : NewLine.values()) {
				if (newLine.toString().equals(characters) || newLine.name().equalsIgnoreCase(characters.trim())) {
					return newLine;
				}
			}
		}
		return null;
	}

}
