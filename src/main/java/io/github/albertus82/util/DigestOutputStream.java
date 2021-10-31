package io.github.albertus82.util;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.xml.bind.DatatypeConverter;

/**
 * This class implements an output stream that can be used to compute the digest
 * of a data stream. The digest value can be retrieved using
 * <code>getValue()</code> (byte array) and <code>toString()</code>
 * (hexadecimal) only when the stream is closed.
 * <p>
 * Closing a {@code DigestOutputStream} completes the hash computation by
 * performing final operations such as padding.
 * </p>
 * 
 * @see MessageDigest
 */
public class DigestOutputStream extends OutputStream {

	private final MessageDigest digest;

	private byte[] result;

	/**
	 * Creates a new DigestOutputStream object. The given digest will be reset.
	 * 
	 * @param digest the digest object associated with this stream instance.
	 */
	public DigestOutputStream(final MessageDigest digest) {
		digest.reset();
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
	 * 
	 * @throws IOException if the stream is closed.
	 */
	@Override
	public synchronized void write(final int b) throws IOException {
		ensureOpen();
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
	 * 
	 * @throws IOException if the stream is closed.
	 */
	@Override
	public synchronized void write(final byte[] b, final int off, final int len) throws IOException {
		ensureOpen();
		digest.update(b, off, len);
	}

	/**
	 * Updates the digest using the specified array of bytes.
	 * 
	 * @param b the array of bytes.
	 * 
	 * @throws IOException if the stream is closed.
	 */
	@Override
	public synchronized void write(final byte[] b) throws IOException {
		ensureOpen();
		digest.update(b);
	}

	/**
	 * Completes the hash computation by performing final operations such as
	 * padding. Call this method before {@link #getValue} or {@link #toString}.
	 */
	@Override
	public synchronized void close() {
		if (result == null) {
			result = digest.digest();
		}
	}

	/**
	 * Returns the digest, or null if the stream is not closed.
	 * 
	 * @return the digest, or null if the stream is not closed
	 */
	public byte[] getValue() {
		return result;
	}

	/**
	 * Returns a string representation of the digest, or an empty string if the
	 * stream is not closed.
	 */
	@Override
	public String toString() {
		final byte[] value = getValue();
		if (value != null) {
			return DatatypeConverter.printHexBinary(value).toLowerCase(Locale.ROOT);
		}
		else {
			return "";
		}
	}

	private void ensureOpen() throws IOException {
		if (result != null) {
			throw new IOException("Stream closed");
		}
	}

}
