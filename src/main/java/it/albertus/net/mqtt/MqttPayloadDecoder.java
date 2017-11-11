package it.albertus.net.mqtt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import it.albertus.util.IOUtils;

/**
 * MQTT payload decoding utility based on {@link MqttPayload} that also provides
 * data decompression support.
 * <p>
 * This class is thread-safe.
 * 
 * @see MqttPayload
 */
public class MqttPayloadDecoder {

	private static final int BUFFER_SIZE = 4096;

	/**
	 * Decodes a MQTT payload based on {@link MqttPayload}; data decompression
	 * is performed if needed.
	 * 
	 * @param payload the received payload, as extracted from the MQTT message.
	 * @return the original payload (data decompression is performed if needed).
	 * @throws IOException in case of malformed payload
	 * 
	 * @see MqttPayload
	 */
	public byte[] decode(final byte[] payload) throws IOException {
		return decode(MqttPayload.fromPayload(payload));
	}

	@SuppressWarnings("restriction")
	byte[] decode(final MqttPayload mqttPayload) throws IOException {
		// Check Content-Length header
		if (mqttPayload.getHeaders().containsKey(MqttUtils.HEADER_KEY_CONTENT_LENGTH)) {
			final int contentLength = Integer.parseInt(mqttPayload.getHeaders().getFirst(MqttUtils.HEADER_KEY_CONTENT_LENGTH));
			if (contentLength != mqttPayload.getBody().length) {
				throw new IOException(MqttUtils.HEADER_KEY_CONTENT_LENGTH + " header value does not match the actual body length (expected: " + contentLength + ", actual: " + mqttPayload.getBody().length + ").");
			}
		}

		// Decompress body if needed
		if (MqttUtils.HEADER_VALUE_GZIP.equalsIgnoreCase(mqttPayload.getHeaders().getFirst(MqttUtils.HEADER_KEY_CONTENT_ENCODING))) {
			return decompress(mqttPayload.getBody());
		}
		else {
			return mqttPayload.getBody();
		}
	}

	protected byte[] decompress(final byte[] compressed) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPInputStream gzis = null;
		try {
			gzis = new GZIPInputStream(new ByteArrayInputStream(compressed));
			IOUtils.copy(gzis, baos, BUFFER_SIZE);
			gzis.close();
		}
		finally {
			IOUtils.closeQuietly(gzis);
		}
		return baos.toByteArray();
	}

}
