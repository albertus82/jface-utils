package it.albertus.util;

import java.util.zip.Checksum;

/**
 * This class implements an output stream that can be used to compute the CRC-16
 * of a data stream. The checksum value can be retrieved using
 * <code>getValue()</code> (long) and <code>toString()</code> (hexadecimal).
 * <p>
 * Closing a <tt>CRC16OutputStream</tt> has no effect. The methods in this class
 * can be called after the stream has been closed without generating an
 * <tt>IOException</tt>.
 * </p>
 * 
 * @see Checksum
 */
public class CRC16OutputStream extends ChecksumOutputStream<CRC16> {

	/**
	 * Creates a new CRC16OutputStream object.
	 */
	public CRC16OutputStream() {
		super(new CRC16(), 16);
	}

	/**
	 * Updates the CRC-16 checksum with the specified array of bytes.
	 *
	 * @param b the array of bytes to update the checksum with
	 */
	@Override
	public void write(final byte[] b) {
		checksum.update(b);
	}

}
