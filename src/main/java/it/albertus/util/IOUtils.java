package it.albertus.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipFile;

public class IOUtils {

	public static final int EOF = -1;

	private IOUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static void closeQuietly(final Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (final IOException ioe) {/* Ignore */}
	}

	public static void closeQuietly(final ZipFile zipFile) {
		try {
			if (zipFile != null) {
				zipFile.close();
			}
		}
		catch (final IOException ioe) {/* Ignore */}
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

}
