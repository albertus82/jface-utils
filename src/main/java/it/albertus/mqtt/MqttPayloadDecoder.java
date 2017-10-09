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

public class MqttPayloadDecoder {

	private static final int BUFFER_SIZE = 4096;

	public byte[] decode(final byte[] receivedPayload) throws IOException {
		final List<byte[]> tokens = split(receivedPayload);

		if (tokens.size() < 2 || tokens.get(tokens.size() - 2).length != 0) {
			throw new IOException("Missing CRLF between headers and body.");
		}
		final byte[] body = tokens.get(tokens.size() - 1);

		final Map<String, String> headers = parseHeaders(tokens);

		if (headers.containsKey(MqttUtils.HEADER_KEY_CONTENT_LENGTH.toLowerCase())) {
			final int contentLength = Integer.parseInt(headers.get(MqttUtils.HEADER_KEY_CONTENT_LENGTH.toLowerCase()));
			if (contentLength != body.length) {
				throw new IOException(MqttUtils.HEADER_KEY_CONTENT_LENGTH + " header value does not match the actual body length (expected: " + contentLength + ", actual: " + body.length + ").");
			}
		}

		if (MqttUtils.HEADER_VALUE_GZIP.equalsIgnoreCase(headers.get(MqttUtils.HEADER_KEY_CONTENT_ENCODING.toLowerCase()))) {
			return decompress(body);
		}
		else {
			return body;
		}
	}

	protected Map<String, String> parseHeaders(final List<byte[]> tokens) {
		final Map<String, String> headers = new HashMap<String, String>();
		for (int i = 0; i < tokens.size() - 2; i++) { // penultimate token should be empty
			final String headerLine = new String(tokens.get(i), MqttUtils.CHARSET_UTF8);
			if (headerLine.indexOf(':') != -1) {
				final String key = headerLine.substring(0, headerLine.indexOf(':')).trim();
				final String value = headerLine.substring(headerLine.indexOf(':') + 1).trim();
				if (!key.isEmpty() && !value.isEmpty()) {
					headers.put(key.toLowerCase(), value);
				}
			}
		}
		return headers;
	}

	protected List<byte[]> split(final byte[] array) {
		final LinkedList<byte[]> byteArrays = new LinkedList<byte[]>();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < array.length; i++) {
			if (array.length > i + 3 && array[i] == MqttUtils.CRLF[0] && array[i + 1] == MqttUtils.CRLF[1] && array[i + 2] == MqttUtils.CRLF[0] && array[i + 3] == MqttUtils.CRLF[1]) {
				byteArrays.add(baos.toByteArray());
				byteArrays.add(new byte[0]);
				byteArrays.add(Arrays.copyOfRange(array, i + 4, array.length));
				break;
			}
			else if (array.length > i + 1 && array[i + 1] == MqttUtils.CRLF[1]) {
				byteArrays.add(baos.toByteArray());
				i++; // skip LF
				baos.reset();
			}
			else {
				baos.write(array[i]);
			}
		}
		return byteArrays;
	}

	protected byte[] decompress(final byte[] buf) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPInputStream gzis = null;
		try {
			gzis = new GZIPInputStream(new ByteArrayInputStream(buf));
			IOUtils.copy(gzis, baos, BUFFER_SIZE);
		}
		finally {
			if (gzis != null) {
				gzis.close();
			}
		}
		return baos.toByteArray();
	}

}
