package it.albertus.util;

import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Checksum;

/**
 * This class implements an output stream that can be used to compute the digest
 * of a data stream. The digest value can be retrieved using
 * <code>getValue()</code> (byte array) and <code>toString()</code>
 * (hexadecimal).
 * <p>
 * Closing a <tt>DigestOutputStream</tt> has no effect. The methods in this
 * class can be called after the stream has been closed without generating an
 * <tt>IOException</tt>.
 * </p>
 * 
 * @see Checksum
 */
public class DigestOutputStream extends OutputStream {

	private final MessageDigest digest;

	/**
	 * Creates a new DigestOutputStream object.
	 * 
	 * @param digest the digest object associated with this stream instance.
	 */
	public DigestOutputStream(final MessageDigest digest) {
		this.digest = digest;
	}

	/**
	 * Creates a new DigestOutputStream object.
	 * 
	 * @param algorithm the algorithm used for the digest.
	 * 
	 * @throws NoSuchAlgorithmException if the argument is not valid.
	 */
	public DigestOutputStream(final String algorithm) throws NoSuchAlgorithmException {
		this.digest = MessageDigest.getInstance(algorithm);
	}

	/**
	 * Updates the digest with the specified byte (the low eight bits of the
	 * argument b).
	 *
	 * @param b the byte with which to update the digest.
	 */
	@Override
	public void write(final int b) {
		digest.update((byte) b);
	}

	/**
	 * Updates the digest using the specified array of bytes, starting at the
	 * specified offset.
	 * 
	 * @param b the array of bytes.
	 *
	 * @param off the offset to start from in the array of bytes.
	 *
	 * @param len the number of bytes to use, starting at <code>offset</code>.
	 */
	@Override
	public void write(final byte[] b, final int off, final int len) {
		digest.update(b, off, len);
	}

	/**
	 * Updates the digest using the specified array of bytes.
	 * 
	 * @param b the array of bytes.
	 */
	@Override
	public void write(final byte[] b) {
		digest.update(b);
	}

	/**
	 * Closing a <tt>DigestOutputStream</tt> has no effect. The methods in this
	 * class can be called after the stream has been closed without generating
	 * an <tt>IOException</tt>.
	 * <p>
	 */
	@Override
	public void close() {/* Do nothing */}

	/**
	 * Completes the hash computation by performing final operations such as
	 * padding. The digest is reset after this call is made.
	 * 
	 */
	public byte[] getValue() {
		return digest.digest();
	}

	/**
	 * Resets the digest for further use.
	 */
	public void reset() {
		digest.reset();
	}

	/**
	 * Returns a string representation of the digest.
	 */
	@Override
	public String toString() {
		return String.valueOf(encodeHex(getValue(), new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }));
	}

	private static char[] encodeHex(final byte[] data, final char[] toDigits) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++, j += 2) {
			out[j] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j + 1] = toDigits[0x0F & data[i]];
		}
		return out;
	}

}
