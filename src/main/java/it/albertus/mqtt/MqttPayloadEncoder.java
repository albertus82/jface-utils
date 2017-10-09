package it.albertus.mqtt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import it.albertus.httpserver.HttpDateGenerator;

public class MqttPayloadEncoder {

	private static final ThreadLocal<HttpDateGenerator> httpDateGenerator = new ThreadLocal<HttpDateGenerator>() {
		@Override
		protected HttpDateGenerator initialValue() {
			return new HttpDateGenerator();
		}
	};

	public byte[] encode(final byte[] payloadToSend) {
		final Map<String, String> headers = new TreeMap<String, String>();
		return buildPayload(headers, payloadToSend);
	}

	public byte[] encode(final byte[] payloadToSend, final boolean compress) throws IOException {
		final Map<String, String> headers = new TreeMap<String, String>();
		final byte[] body;

		if (compress) {
			body = compress(payloadToSend);
			headers.put(MqttUtils.HEADER_KEY_CONTENT_ENCODING, MqttUtils.HEADER_VALUE_GZIP);
		}
		else {
			body = payloadToSend;
		}

		return buildPayload(headers, body);
	}

	protected byte[] buildPayload(final Map<String, String> headers, final byte[] body) {
		headers.put(MqttUtils.HEADER_KEY_CONTENT_LENGTH, Integer.toString(body.length));
		headers.put(MqttUtils.HEADER_KEY_DATE, httpDateGenerator.get().format(new Date()));

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
			throw new IllegalStateException(e); // UTF-8 must be supported by any JVM
		}
	}

	protected byte[] compress(final byte[] payloadToSend) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzos = null;
		try {
			gzos = new GZIPOutputStream(baos);
			gzos.write(payloadToSend);
		}
		finally {
			if (gzos != null) {
				gzos.close();
			}
		}
		return baos.toByteArray();
	}

}
