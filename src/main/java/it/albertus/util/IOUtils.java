package it.albertus.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import it.albertus.util.logging.LoggerFactory;

public class IOUtils {

	private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

	public static final int EOF = -1;

	private IOUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static void closeQuietly(final Closeable... closeables) {
		for (final Closeable closeable : closeables) {
			closeQuietly(closeable);
		}
	}

	public static void closeQuietly(final Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (final IOException e) {
			log.log(Level.FINE, "An error occurred while closing the resource:", e);
		}
	}

	public static void closeQuietly(final ZipFile zipFile) {
		try {
			if (zipFile != null) {
				zipFile.close();
			}
		}
		catch (final IOException e) {
			log.log(Level.FINE, "An error occurred while closing the ZIP file:", e);
		}
	}

	public static long copy(final InputStream input, final OutputStream output, final byte[] buffer) throws IOException {
		long count = 0;
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static long copy(final InputStream input, final OutputStream output, final int bufferSize) throws IOException {
		return copy(input, output, new byte[bufferSize]);
	}

	public static long copy(final Reader input, final Writer output, final char[] buffer) throws IOException {
		long count = 0;
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static long copy(final Reader input, final Writer output, final int bufferSize) throws IOException {
		return copy(input, output, new char[bufferSize]);
	}

}
