package it.albertus.util;

public class DigestUtils {

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private DigestUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static char[] encodeHex(final byte[] data, final char[] toDigits) {
		if (data == null) {
			return new char[0];
		}
		final int l = data.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++, j += 2) {
			out[j] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j + 1] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	public static char[] encodeHex(final byte[] data) {
		return encodeHex(data, DIGITS);
	}

}
