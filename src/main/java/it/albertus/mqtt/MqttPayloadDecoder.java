package it.albertus.mqtt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import it.albertus.util.IOUtils;
import it.albertus.util.NewLine;

public class MqttPayloadDecoder {

	private static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";

	private static final String CHARSET = "UTF-8";

	public byte[] decode(final byte[] receivedPayload) throws IOException {
		final List<byte[]> tokens = split(receivedPayload, NewLine.CRLF.toString().getBytes(CHARSET));
		final Map<String, String> headers = new HashMap<String, String>();
		for (int i = 0; i < tokens.size() - 2; i++) {
			final String headerLine = new String(tokens.get(i), CHARSET);
			final String key = headerLine.substring(0, headerLine.indexOf(':')).trim().toLowerCase();
			final String value = headerLine.substring(headerLine.indexOf(':') + 1).trim();
			headers.put(key, value);
		}
		final byte[] buf = tokens.get(tokens.size() - 1);

		if (headers.containsKey(HEADER_CONTENT_LENGTH.toLowerCase())) {
			final int contentLength = Integer.parseInt(headers.get(HEADER_CONTENT_LENGTH.toLowerCase()));
			if (contentLength != buf.length) {
				throw new IOException(HEADER_CONTENT_LENGTH + " header value does not match the actual length (expected: " + contentLength + ", actual: " + buf.length + ").");
			}
		}

		if ("gzip".equalsIgnoreCase(headers.get(HEADER_CONTENT_ENCODING.toLowerCase()))) {
			return decompress(buf);
		}
		else {
			return buf;
		}
	}

	protected List<byte[]> split(final byte[] array, final byte[] delimiter) {
		final List<byte[]> byteArrays = new LinkedList<byte[]>();
		if (delimiter.length == 0) {
			return byteArrays;
		}
		int begin = 0;

		outer: for (int i = 0; i < array.length - delimiter.length + 1; i++) {
			for (int j = 0; j < delimiter.length; j++) {
				if (array[i + j] != delimiter[j]) {
					continue outer;
				}
			}

			// If delimiter is at the beginning then there will not be any data.
			if (begin != i)
				byteArrays.add(Arrays.copyOfRange(array, begin, i));
			begin = i + delimiter.length;
		}

		// delimiter at the very end with no data following?
		if (begin != array.length)
			byteArrays.add(Arrays.copyOfRange(array, begin, array.length));

		return byteArrays;
	}

	protected byte[] decompress(final byte[] buf) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPInputStream gzis = null;
		try {
			gzis = new GZIPInputStream(new ByteArrayInputStream(buf));
			IOUtils.copy(gzis, baos, 8192);
		}
		finally {
			if (gzis != null) {
				gzis.close();
			}
		}
		return baos.toByteArray();
	}

}
