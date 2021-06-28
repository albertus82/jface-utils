package it.albertus.util;

import java.io.OutputStream;
import java.util.zip.Checksum;

/**
 * This class implements an output stream that can be used to compute the
 * checksum of a data stream. The checksum value can be retrieved using
 * <code>getValue()</code> (long) and <code>toString()</code> (hexadecimal).
 * <p>
 * Closing a <tt>ChecksumOutputStream</tt> has no effect. The methods in this
 * class can be called after the stream has been closed without generating an
 * <tt>IOException</tt>.
 * </p>
 * 
 * @see Checksum
 */
public class ChecksumOutputStream<T extends Checksum> extends OutputStream {

	protected final T checksum;
	protected final int bits;

	/**
	 * Creates a new ChecksumOutputStream object.
	 * 
	 * @param checksum the checksum object associated with this stream instance.
	 */
	public ChecksumOutputStream(final T checksum, final int bits) {
		this.checksum = checksum;
		this.bits = bits;
	}

	/**
	 * Updates the checksum with the specified byte (the low eight bits of the
	 * argument b).
	 *
	 * @param b the byte to update the checksum with
	 */
	@Override
	public void write(final int b) {
		checksum.update(b);
	}

	/**
	 * Updates the checksum with the specified array of bytes.
	 */
	@Override
	public void write(final byte[] b, final int off, final int len) {
		checksum.update(b, off, len);
	}

	/**
	 * Closing a <tt>ChecksumOutputStream</tt> has no effect. The methods in this
	 * class can be called after the stream has been closed without generating an
	 * <tt>IOException</tt>.
	 * <p>
	 */
	@Override
	public void close() {/* Do nothing */}

	/**
	 * Returns checksum value.
	 */
	public long getValue() {
		return checksum.getValue();
	}

	/**
	 * Resets checksum to initial value.
	 */
	public void reset() {
		checksum.reset();
	}

	/**
	 * Returns a string representation of the checksum as an unsigned integer in
	 * base&nbsp;16.
	 */
	@Override
	public String toString() {
		return String.format("%0" + bits / 4 + "x", checksum.getValue()); // NOSONAR
	}

}
