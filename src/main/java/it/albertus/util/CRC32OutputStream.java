package it.albertus.util;

import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * This class implements an output stream that can be used to compute the CRC-32
 * of a data stream. The checksum value can be retrieved using
 * <code>getValue()</code> (long) and <code>toString()</code> (hexadecimal).
 * <p>
 * Closing a <tt>ByteArrayOutputStream</tt> has no effect. The methods in this
 * class can be called after the stream has been closed without generating an
 * <tt>IOException</tt>.
 * </p>
 * 
 * @see Checksum
 */
public class CRC32OutputStream extends OutputStream {

	private final CRC32 crc32;

	/**
	 * Creates a new CRC32OutputStream object.
	 */
	public CRC32OutputStream() {
		crc32 = new CRC32();
	}

	/**
	 * Updates the CRC-32 checksum with the specified byte (the low eight bits
	 * of the argument b).
	 *
	 * @param b the byte to update the checksum with
	 */
	@Override
	public void write(final int b) {
		crc32.update(b);
	}

	/**
	 * Updates the CRC-32 checksum with the specified array of bytes.
	 *
	 * @param b the array of bytes to update the checksum with
	 */
	@Override
	public void write(final byte[] b) {
		crc32.update(b);
	}

	/**
	 * Updates the CRC-32 checksum with the specified array of bytes.
	 */
	@Override
	public void write(final byte[] b, final int off, final int len) {
		crc32.update(b, off, len);
	}

	/**
	 * Closing a <tt>CRC32OutputStream</tt> has no effect. The methods in this
	 * class can be called after the stream has been closed without generating
	 * an <tt>IOException</tt>.
	 * <p>
	 */
	@Override
	public void close() {/* Do nothing */}

	/**
	 * Returns CRC-32 value.
	 */
	public long getValue() {
		return crc32.getValue();
	}

	/**
	 * Resets CRC-32 to initial value.
	 */
	public void reset() {
		crc32.reset();
	}

	/**
	 * Returns a string representation of the checksum as an unsigned integer in
	 * base&nbsp;16.
	 */
	@Override
	public String toString() {
		return Long.toHexString(crc32.getValue());
	}

}
