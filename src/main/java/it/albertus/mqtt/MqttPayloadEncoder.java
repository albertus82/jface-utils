package it.albertus.mqtt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import it.albertus.httpserver.HttpDateGenerator;
import it.albertus.util.IOUtils;

public class MqttPayloadEncoder {

	private static final ThreadLocal<HttpDateGenerator> httpDateGenerator = new ThreadLocal<HttpDateGenerator>() {
		@Override
		protected HttpDateGenerator initialValue() {
			return new HttpDateGenerator();
		}
	};

	@SuppressWarnings("restriction")
	public MqttPayload encode(final byte[] payloadToSend, final boolean compress) {
		final MqttPayload payload;
		if (compress) {
			payload = new MqttPayload(compress(payloadToSend));
			payload.getHeaders().set(MqttUtils.HEADER_KEY_CONTENT_ENCODING, MqttUtils.HEADER_VALUE_GZIP);
		}
		else {
			payload = new MqttPayload(payloadToSend);
		}
		payload.getHeaders().set(MqttUtils.HEADER_KEY_DATE, httpDateGenerator.get().format(new Date()));
		return payload;
	}

	protected byte[] compress(final byte[] uncompressed) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzos = null;
		try {
			gzos = new GZIPOutputStream(baos);
			gzos.write(uncompressed);
			gzos.close(); // ensure that all bytes are written to the output stream
		}
		catch (final IOException e) {
			throw new IllegalStateException(e); // ByteArrayOutputStream cannot throw IOException
		}
		finally {
			IOUtils.closeQuietly(gzos);
		}
		return baos.toByteArray();
	}

}
