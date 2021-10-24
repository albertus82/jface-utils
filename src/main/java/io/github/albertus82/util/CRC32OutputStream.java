package io.github.albertus82.util;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * This class implements an output stream that can be used to compute the CRC-32
 * of a data stream. The checksum value can be retrieved using
 * <code>getValue()</code> (long) and <code>toString()</code> (hexadecimal).
 * <p>
 * Closing a <tt>CRC32OutputStream</tt> has no effect. The methods in this class
 * can be called after the stream has been closed without generating an
 * <tt>IOException</tt>.
 * </p>
 * 
 * @see Checksum
 */
public class CRC32OutputStream extends ChecksumOutputStream<CRC32> {

	/**
	 * Creates a new CRC32OutputStream object.
	 */
	public CRC32OutputStream() {
		super(new CRC32(), 32);
	}

	/**
	 * Updates the CRC-32 checksum with the specified array of bytes.
	 *
	 * @param b the array of bytes to update the checksum with
	 */
	@Override
	public void write(final byte[] b) {
		checksum.update(b);
	}

}
