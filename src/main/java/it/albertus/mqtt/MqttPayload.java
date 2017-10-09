package it.albertus.mqtt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MqttPayload implements Serializable {

	private static final long serialVersionUID = -5028888118840493965L;

	private final Map<String, String> headers = new LinkedHashMap<String, String>(8) {

		private static final long serialVersionUID = -2145390760920797996L;

		@Override
		public boolean containsKey(final Object key) {
			final String keyStr = key.toString();
			for (final String k : headers.keySet()) {
				if (keyStr.equalsIgnoreCase(k)) {
					return true;
				}
			}
			return false;
		}
	};

	private final byte[] body;

	public MqttPayload(final byte[] body) {
		if (body == null) {
			throw new NullPointerException("body cannot be null");
		}
		this.body = body;
	}

	public Map<String, String> getHeaders() {
		return headers;
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

}
