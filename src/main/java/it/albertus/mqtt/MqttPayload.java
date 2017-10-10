package it.albertus.mqtt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;

import it.albertus.util.NewLine;

@SuppressWarnings("restriction")
public class MqttPayload {

	private static final byte[] CRLF = NewLine.CRLF.toString().getBytes(MqttUtils.CHARSET_UTF8);
	private static final byte CR = CRLF[0];
	private static final byte LF = CRLF[1];

	private final Headers headers = new Headers();

	private final byte[] body;

	public MqttPayload(final byte[] body) {
		if (body == null) {
			throw new NullPointerException("body cannot be null");
		}
		this.body = body;
	}

	public static MqttPayload fromPayload(final byte[] payload) throws IOException {
		final List<byte[]> tokens = split(payload);

		if (tokens.size() < 2 || tokens.get(tokens.size() - 2).length != 0) {
			throw new IOException("Missing CRLF between headers and body.");
		}
		final byte[] body = tokens.get(tokens.size() - 1);

		final MqttPayload instance = new MqttPayload(body);
		parseHeaders(tokens.subList(0, tokens.size() - 2), instance.headers);

		return instance;
	}

	public Headers getHeaders() {
		return headers;
	}

	public byte[] getBody() {
		return body;
	}

	public byte[] toByteArray() {
		// Ensure there is at least a Content-Length header
		if (!headers.containsKey(MqttUtils.HEADER_KEY_CONTENT_LENGTH)) {
			headers.set(MqttUtils.HEADER_KEY_CONTENT_LENGTH, Integer.toString(body.length));
		}

		// Build the effective payload
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			writeHeaders(headers, baos);
			baos.write(CRLF);
			baos.write(body);
			return baos.toByteArray();
		}
		catch (final IOException e) {
			throw new IllegalStateException(e); // ByteArrayOutputStream cannot throw IOException
		}
	}

	private static void writeHeaders(final Headers source, final ByteArrayOutputStream destination) throws IOException {
		for (final Entry<String, List<String>> entry : source.entrySet()) {
			if (entry.getKey() != null) {
				final String key = entry.getKey().trim();
				if (!key.isEmpty()) {
					for (final String value : entry.getValue()) {
						destination.write((key + ": " + (value != null ? value.trim() : "")).getBytes(MqttUtils.CHARSET_UTF8));
						destination.write(CRLF);
					}
				}
			}
		}
	}

	private static void parseHeaders(final Iterable<byte[]> source, final Headers destination) {
		for (final byte[] token : source) {
			final String headerLine = new String(token, MqttUtils.CHARSET_UTF8);
			final int indexOfColon = headerLine.indexOf(':');
			if (indexOfColon != -1) {
				final String key = headerLine.substring(0, indexOfColon).trim();
				if (!key.isEmpty()) {
					destination.add(key, headerLine.substring(indexOfColon + 1).trim());
				}
			}
		}
	}

	static List<byte[]> split(final byte[] payload) {
		final List<byte[]> byteArrays = new ArrayList<byte[]>();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < payload.length; i++) {
			if (payload.length > i + 3 && payload[i] == CR && payload[i + 1] == LF && payload[i + 2] == CR && payload[i + 3] == LF) {
				byteArrays.add(baos.toByteArray());
				byteArrays.add(new byte[0]);
				byteArrays.add(Arrays.copyOfRange(payload, i + 4, payload.length));
				break;
			}
			else if (payload.length > i + 1 && payload[i + 1] == LF) {
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
