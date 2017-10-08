package it.albertus.mqtt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import it.albertus.httpserver.HttpDateGenerator;
import it.albertus.util.NewLine;

public class MqttPayloadEncoder {

	private static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String HEADER_DATE = "Date";

	private static final String CHARSET = "UTF-8";
	private static final byte[] CRLF;

	static {
		try {
			CRLF = NewLine.CRLF.toString().getBytes(CHARSET);
		}
		catch (final UnsupportedEncodingException e) {
			throw new IllegalStateException(e); // UTF-8 must be supported by any JVM
		}
	}

	private static final ThreadLocal<HttpDateGenerator> httpDateGenerator = new ThreadLocal<HttpDateGenerator>() {
		@Override
		protected HttpDateGenerator initialValue() {
			return new HttpDateGenerator();
		}
	};

	public byte[] encode(final String str) {
		try {
			return encode(str.getBytes(CHARSET));
		}
		catch (final UnsupportedEncodingException e) {
			throw new IllegalStateException(e); // UTF-8 must be supported by any JVM
		}
	}

	public byte[] encode(final String str, final boolean compress) throws IOException {
		try {
			return encode(str.getBytes(CHARSET), compress);
		}
		catch (final UnsupportedEncodingException e) {
			throw new IllegalStateException(e); // UTF-8 must be supported by any JVM
		}
	}

	public byte[] encode(final byte[] payloadToSend) {
		final Map<String, String> headers = new TreeMap<String, String>();
		return buildPayload(headers, payloadToSend);
	}

	public byte[] encode(final byte[] payloadToSend, final boolean compress) throws IOException {
		final Map<String, String> headers = new TreeMap<String, String>();
		final byte[] body;

		if (compress) {
			body = compress(payloadToSend);
			headers.put(HEADER_CONTENT_ENCODING, "gzip");
		}
		else {
			body = payloadToSend;
		}

		return buildPayload(headers, body);
	}

	protected byte[] buildPayload(final Map<String, String> headers, final byte[] body) {
		headers.put(HEADER_CONTENT_LENGTH, Integer.toString(body.length));
		headers.put(HEADER_DATE, httpDateGenerator.get().format(new Date()));

		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (final Entry<String, String> header : headers.entrySet()) {
				baos.write((header.getKey() + ": " + header.getValue()).getBytes(CHARSET));
				baos.write(CRLF);
			}
			baos.write(CRLF);
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
