package io.github.albertus82.net.mqtt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import io.github.albertus82.util.IOUtils;

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

	private final int bufferSize;

	/**
	 * Creates a MQTT Payload Decoder that uses a default-sized buffer.
	 */
	public MqttPayloadDecoder() {
		this(BUFFER_SIZE);
	}

	/**
	 * Creates a MQTT Payload Decoder that uses a buffer of the specified size.
	 *
	 * @param bufferSize buffer size
	 *
	 * @exception IllegalArgumentException If bufferSize is <= 0
	 */
	public MqttPayloadDecoder(final int bufferSize) {
		if (bufferSize <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		this.bufferSize = bufferSize;
	}

	/**
	 * Decodes a MQTT payload based on {@link MqttPayload}; data decompression is
	 * performed if needed.
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
			IOUtils.copy(gzis, baos, bufferSize);
			gzis.close();
		}
		finally {
			IOUtils.closeQuietly(gzis);
		}
		return baos.toByteArray();
	}

}
