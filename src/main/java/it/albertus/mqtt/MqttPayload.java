package it.albertus.mqtt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;

import it.albertus.util.NewLine;

/**
 * Abstraction on the MQTT payload (byte array) that allows adding headers to
 * the message in order to make the protocol more extensible.
 * <p>
 * The structure of the resulting payload is similar to the
 * <a href="http://httpwg.org/specs/rfc7230.html#http.message">HTTP response
 * message</a>: there are one or more header fields in the form of
 * <tt>Header-Name: value</tt> separated by <tt>CRLF</tt>. A double CRLF (null
 * line after the last header) marks the end of the header section and the
 * beginning of the <em>effective payload</em>, a.k.a. <em>body</em>:
 * 
 * <pre>
 * Content-length: 52
 * X-custom-header: Custom value
 * 
 * This is the effective payload of the message (body).
 * </pre>
 * 
 * Unlike HTTP, there is no start line. Existing HTTP header names should be
 * preferred whenever possible. Header names are case-insensitive. The
 * <tt>Content-Length</tt> header is mandatory and its value must match the
 * length of the body, ignoring the size of the headers section. The headers
 * section should contain only US-ASCII characters (0-127); in any case, the
 * headers section must adopt UTF-8 charset encoding.
 * <p>
 * Other headers can be added, e.g., <tt>Date</tt> (the RFC 1123 format used for
 * HTTP is recommended), or <tt>Content-Encoding</tt> in order to add support
 * for data compression (i.e.: <tt>Content-Encoding: gzip</tt>). Please note
 * that this class does not perform any encoding or decoding, it only manages
 * the structure of the payload. Note also that if the body is compressed using
 * some algorithm, the <tt>Content-Length</tt> header value must always match
 * the compressed length.
 * <p>
 * <b>If you want to build a new MQTT message payload to publish</b>, start by
 * calling the constuctor {@link #MqttPayload(byte[])} providing as argument
 * your <em>effective payload</em>, then you can invoke {@link #getHeaders()} on
 * the newly created object to manage the headers. Eventually you should call
 * {@link #toPayload()} in order to obtain the complete payload (headers and
 * body) to publish. If you didn't set a <tt>Content-Length</tt> header, the
 * {@link #toPayload()} will compute and add one based on the <em>effective
 * payload</em> length.
 * <p>
 * <b>If you want to parse a received MQTT message payload</b> that was
 * generated following the aforementioned rules, you can invoke the static
 * factory method {@link #fromPayload(byte[])} providing as argument the
 * received payload; then you can access headers and body using the getter
 * methods on the returned object as usual.
 * 
 * @see <a href="http://httpwg.org">HTTP</a>
 */
@SuppressWarnings("restriction")
public class MqttPayload {

	private static final byte[] CRLF = NewLine.CRLF.toString().getBytes(MqttUtils.CHARSET_UTF8);
	private static final byte CR = CRLF[0];
	private static final byte LF = CRLF[1];

	private final Headers headers = new Headers();

	private final byte[] body;

	/**
	 * Creates a new instance with the provided <em>effective payload</em>.
	 * <p>
	 * Caller can mange the headers invoking {@link #getHeaders()} on the
	 * created object, and eventually must call {@link #toPayload()} in order to
	 * obtain the full payload (byte array) to send as MQTT message.
	 * 
	 * @param body the <em>effective payload</em>
	 */
	public MqttPayload(final byte[] body) {
		if (body == null) {
			throw new NullPointerException("body cannot be null");
		}
		this.body = body;
	}

	/**
	 * Parse a received MQTT message payload and returns a new instance.
	 * 
	 * @param payload the full payload received
	 * @return an instance based on the provided payload
	 * @throws IOException if the provided payload is malformed or invalid
	 */
	public static MqttPayload fromPayload(final byte[] payload) throws IOException {
		final List<byte[]> tokens = split(payload);

		if (tokens.size() < 2 || tokens.get(tokens.size() - 2).length != 0) {
			throw new IOException("Missing null line (double CRLF) between headers and body.");
		}
		final byte[] body = tokens.get(tokens.size() - 1);

		final MqttPayload instance = new MqttPayload(body);
		parseHeaders(tokens.subList(0, tokens.size() - 2), instance.headers);

		return instance;
	}

	/**
	 * Returns the headers of this payload. Headers can be managed using the
	 * returned object.
	 * 
	 * @return the headers of this payload
	 */
	public Headers getHeaders() {
		return headers;
	}

	/**
	 * Returns the <em>effective payload</em> of this payload. Modifying the
	 * returned array is discouraged.
	 * 
	 * @return the <em>effective payload</em> of this payload
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * Generates the full MQTT message payload to publish. A
	 * <tt>Content-Length</tt> header will be added if not present, and its
	 * value will be determined by the <em>effective payload</em> length.
	 * 
	 * @return the byte array (full MQTT payload) to publish
	 */
	public byte[] toPayload() {
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
						destination.write((key + ": " + (value != null ? value.trim() : "")).trim().getBytes(MqttUtils.CHARSET_UTF8));
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

	private static List<byte[]> split(final byte[] payload) {
		final List<byte[]> byteArrays = new ArrayList<byte[]>();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < payload.length; i++) {
			if (payload.length > i + 3 && payload[i] == CR && payload[i + 1] == LF && payload[i + 2] == CR && payload[i + 3] == LF) {
				byteArrays.add(baos.toByteArray());
				byteArrays.add(new byte[0]);
				byteArrays.add(Arrays.copyOfRange(payload, i + 4, payload.length));
				break;
			}
			else if (payload.length > i + 1 && payload[i] == CR && payload[i + 1] == LF) {
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
