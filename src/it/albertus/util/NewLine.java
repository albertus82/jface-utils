package it.albertus.util;

public enum NewLine {

	CR('\r'),
	LF('\n'),
	CRLF('\r', '\n');

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
	public char[] getCharacters() {
		return characters;
	}

	/**
	 * Restituisce una stringa contenente la sequenza di caratteri che
	 * rappresenta il ritorno a capo.
	 * 
	 * @return la stringa che rappresenta il ritorno a capo.
	 */
	public String getString() {
		return string;
	}

	/**
	 * Restituisce l'<b>enum</b> corrispondente al tipo di ritorno a capo
	 * desiderato.
	 * 
	 * @param type
	 *            una stringa tra "CR" (Macintosh), "LF" (Unix) e "CRLF"
	 *            (DOS/Windows).
	 * @return l'<b>enum</b> corrispondente al tipo di ritorno a capo passato
	 *         come parametro.
	 */
	public static NewLine getEnum(String type) {
		if (type != null && type.trim().length() != 0) {
			type = type.trim().toUpperCase();
			for (NewLine newLine : NewLine.values()) {
				if (newLine.name().equals(type)) {
					return newLine;
				}
			}
		}
		return null;
	}

}
