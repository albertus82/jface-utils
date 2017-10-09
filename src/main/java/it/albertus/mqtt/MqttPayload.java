package it.albertus.mqtt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MqttPayload implements Serializable {

	private static final long serialVersionUID = -5028888118840493965L;

	private final MqttHeaders headers = new MqttHeaders();

	private final byte[] body;

	public MqttPayload(final byte[] body) {
		if (body == null) {
			throw new NullPointerException("body cannot be null");
		}
		this.body = body;
	}

	public MqttHeaders getHeaders() {
		return headers;
	}

	public byte[] getBody() {
		return body;
	}

	public byte[] toByteArray() {
		// Ensure there is at least a Content-Length header
		if (!headers.containsKey(MqttUtils.HEADER_KEY_CONTENT_LENGTH)) {
			headers.put(MqttUtils.HEADER_KEY_CONTENT_LENGTH, Integer.toString(body.length));
		}

		// Build the effective payload
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (final Entry<String, String> header : headers.entrySet()) {
				baos.write((header.getKey() + ": " + header.getValue()).getBytes(MqttUtils.CHARSET_UTF8));
				baos.write(MqttUtils.CRLF);
			}
			baos.write(MqttUtils.CRLF);
			baos.write(body);
			return baos.toByteArray();
		}
		catch (final IOException e) {
			throw new IllegalStateException(e); // ByteArrayOutputStream cannot throw IOException
		}
	}

	public static MqttPayload fromPayload(final byte[] payload) throws IOException {
		final List<byte[]> tokens = split(payload);

		if (tokens.size() < 2 || tokens.get(tokens.size() - 2).length != 0) {
			throw new IOException("Missing CRLF between headers and body.");
		}
		final byte[] body = tokens.get(tokens.size() - 1);

		final MqttPayload instance = new MqttPayload(body);
		setHeaders(tokens, instance.headers);

		return instance;
	}

	private static Map<String, String> setHeaders(final List<byte[]> tokens, final MqttHeaders headers) {
		for (int i = 0; i < tokens.size() - 2; i++) { // penultimate token should be empty
			final String headerLine = new String(tokens.get(i), MqttUtils.CHARSET_UTF8);
			final int indexOfColon = headerLine.indexOf(':');
			if (indexOfColon != -1) {
				final String key = headerLine.substring(0, indexOfColon).trim();
				final String value = headerLine.substring(indexOfColon + 1).trim();
				if (!key.isEmpty() && !value.isEmpty()) {
					headers.put(key, value);
				}
			}
		}
		return headers;
	}

	static List<byte[]> split(final byte[] payload) {
		final List<byte[]> byteArrays = new LinkedList<byte[]>();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < payload.length; i++) {
			if (payload.length > i + 3 && payload[i] == MqttUtils.CRLF[0] && payload[i + 1] == MqttUtils.CRLF[1] && payload[i + 2] == MqttUtils.CRLF[0] && payload[i + 3] == MqttUtils.CRLF[1]) {
				byteArrays.add(baos.toByteArray());
				byteArrays.add(new byte[0]);
				byteArrays.add(Arrays.copyOfRange(payload, i + 4, payload.length));
				break;
			}
			else if (payload.length > i + 1 && payload[i + 1] == MqttUtils.CRLF[1]) {
				byteArrays.add(baos.toByteArray());
				i++; // skip LF
				baos.reset();
			}
			else {
				baos.write(payload[i]);
			}
		}
		return byteArrays;
	}

}
