package it.albertus.net.mqtt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.albertus.util.NewLine;
import it.albertus.util.logging.LoggerFactory;

@SuppressWarnings("restriction")
public class MqttPayloadTest {

	private static final Logger logger = LoggerFactory.getLogger(MqttPayloadTest.class);

	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	private static final String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." + NewLine.CRLF + "Mauris pretium eget ligula vehicula tempus. Proin accumsan varius sem non blandit." + NewLine.LF + "Nam consectetur magna eu cursus condimentum. Nunc volutpat tellus velit. Nullam elementum vel nisi at euismod." + NewLine.CRLF + "Pellentesque et ante nibh. Donec et leo varius, volutpat libero sit amet, vulputate sem." + NewLine.CR + "Vivamus tempus mi est, malesuada tincidunt mi consequat id. Nunc neque ligula, interdum ut feugiat \u00E8get, imperdiet sed orci." + NewLine.CR;

	private static MqttPayloadDecoder decoder;
	private static MqttPayloadEncoder encoder;

	@BeforeClass
	public static void init() {
		encoder = new MqttPayloadEncoder();
		decoder = new MqttPayloadDecoder();
	}

	@Test
	public void testEncodeDecodeEmptyPayload() throws IOException {
		Assert.assertArrayEquals("Payloads don't match", new byte[] {}, decoder.decode(encoder.encode("".getBytes(), false).toPayload()));
		Assert.assertArrayEquals("Payloads don't match", new byte[] {}, decoder.decode(encoder.encode(new byte[0], true).toPayload()));

		final MqttPayload mp1 = new MqttPayload(new byte[0]);
		mp1.getHeaders().add("X-Custom-Header", "1234567890");
		mp1.getHeaders().add("qwertyuiop", "asdfghjkl");

		final MqttPayload mp2 = MqttPayload.fromPayload(mp1.toPayload());
		Assert.assertEquals("Invalid headers count", 3, mp2.getHeaders().size()); // MqttPayload.toByteArray adds Content-Length
		Assert.assertEquals("Invalid header value", "0", mp2.getHeaders().get("content-length").get(0));

		Assert.assertArrayEquals("Payloads don't match", new byte[] {}, mp2.getBody());

		final StringBuilder payload = new StringBuilder();
		payload.append("X-Custom-Header: zxcvbnm").append(NewLine.CRLF);
		payload.append(NewLine.CRLF);

		final MqttPayload mp3 = MqttPayload.fromPayload(payload.toString().getBytes(CHARSET_UTF8));
		Assert.assertEquals("Invalid headers count", 1, mp3.getHeaders().size());
		Assert.assertEquals("Invalid header value", "zxcvbnm", mp3.getHeaders().getFirst("X-Custom-Header"));
		Assert.assertArrayEquals("Payloads don't match", new byte[] {}, mp3.getBody());
	}

	@Test
	public void testDecodeHeadersBody() throws IOException {
		final StringBuilder payload = new StringBuilder();
		payload.append("Content-Encoding: identity").append(NewLine.CRLF);
		payload.append("Content-length: ").append(text.getBytes(CHARSET_UTF8).length).append(NewLine.CRLF);
		payload.append(NewLine.CRLF);
		payload.append(text);

		final byte[] bytes = payload.toString().getBytes(CHARSET_UTF8);

		Assert.assertArrayEquals("Payloads don't match", text.getBytes(CHARSET_UTF8), decoder.decode(bytes));
	}

	@Test
	public void testRetainForcedHeader() throws IOException {
		final MqttPayload mp1 = new MqttPayload(text.getBytes(CHARSET_UTF8));
		mp1.getHeaders().add("Content-length", "99999"); // Force invalid Content-Length
		mp1.getHeaders().add("Content-Encoding", "identity");
		final byte[] p1 = mp1.toPayload();

		final MqttPayload mp2 = MqttPayload.fromPayload(p1);
		Assert.assertEquals("Invalid headers count", 2, mp2.getHeaders().size());
		Assert.assertEquals("Invalid header value", "99999", mp2.getHeaders().getFirst("Content-length"));
		Assert.assertEquals("Invalid header value", "identity", mp2.getHeaders().getFirst("CONTENT-ENCODING"));
		Assert.assertArrayEquals("Payloads don't match", text.getBytes(CHARSET_UTF8), mp2.getBody());

		try {
			decoder.decode(p1); // must throw exception (invalid Content-Length value)
			Assert.assertTrue("Content-Length check malfunction", false);
		}
		catch (final IOException e) {
			logger.log(Level.INFO, e.toString(), e);
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testAutomaticContentLengthHeader() throws IOException {
		final MqttPayload mp1 = new MqttPayload(text.getBytes(CHARSET_UTF8));
		mp1.getHeaders().add("Content-Encoding", "identity");
		final byte[] payload = mp1.toPayload();

		final MqttPayload mp2 = MqttPayload.fromPayload(payload);
		Assert.assertEquals("Invalid headers count", 2, mp2.getHeaders().size());
		Assert.assertEquals("Invalid header value", Integer.toString(text.getBytes(CHARSET_UTF8).length), mp2.getHeaders().getFirst("Content-length"));
		Assert.assertEquals("Invalid header value", "identity", mp2.getHeaders().getFirst("CONTENT-ENCODING"));
		Assert.assertArrayEquals("Payloads don't match", text.getBytes(CHARSET_UTF8), mp2.getBody());
	}

	@Test
	public void testEncodeDecodeHeadersBody() throws IOException {
		final MqttPayload mp1 = encoder.encode(text.getBytes(CHARSET_UTF8), false);

		// Add some custom headers
		final Map<String, String> customHeaders = new HashMap<String, String>();
		customHeaders.put("X-Custom-Header", "Custom value");
		customHeaders.put("X-Empty-Val-Header", "");
		//	customHeaders.put("X-Null-Val-Header", null);
		customHeaders.put("Content-Encoding", "identity");

		for (final Entry<String, String> e : customHeaders.entrySet()) {
			mp1.getHeaders().set(e.getKey(), e.getValue());
		}

		final byte[] encoded = mp1.toPayload();

		final MqttPayload mp2 = MqttPayload.fromPayload(encoded);

		// Check headers count 
		Assert.assertEquals("Invalid headers count", customHeaders.size() + 2, mp2.getHeaders().size());

		// Headers added by MqttPayloadEncoder
		Assert.assertTrue("Missing header", mp2.getHeaders().containsKey("date"));
		Assert.assertTrue("Missing header", mp2.getHeaders().containsKey("content-Length"));

		// Headers added manually
		Assert.assertEquals("Invalid header value", "Custom value", mp2.getHeaders().getFirst("X-Custom-Header"));
		Assert.assertEquals("Invalid header value", "", mp2.getHeaders().getFirst("X-Empty-Val-Header"));
		//	Assert.assertEquals("Invalid header value", "", mp2.getHeaders().getFirst("X-Null-Val-Header"));
		Assert.assertEquals("Invalid header value", "identity", mp2.getHeaders().getFirst("Content-Encoding"));

		final byte[] decoded = decoder.decode(mp2);

		// Check effective payload
		Assert.assertArrayEquals("Payloads don't match", text.getBytes(CHARSET_UTF8), decoded);
	}

	@Test
	public void testDecodeInvalidHeaders() throws IOException {
		final StringBuilder sb = new StringBuilder();

		// Valid (sometimes strange) headers
		sb.append("   Content-Length :  ").append(text.getBytes(CHARSET_UTF8).length).append(NewLine.CRLF);
		sb.append("Content-Encoding\t:     xyzw     ").append(NewLine.CRLF);
		sb.append("Valid Header-Without Value:").append(NewLine.CRLF);

		// Invalid headers
		sb.append("Header-Without-Comma").append(NewLine.CRLF);
		sb.append(": Header Without Key ").append(NewLine.CRLF);
		sb.append(" :Another Header Without Key").append(NewLine.CRLF);

		sb.append(NewLine.CRLF);
		sb.append(text);

		final MqttPayload mp = MqttPayload.fromPayload(sb.toString().getBytes(CHARSET_UTF8));

		// Check headers count (invalid headers should be removed)
		Assert.assertEquals("Invalid headers count", 3, mp.getHeaders().size());

		// Check values for valid headers
		Assert.assertEquals("Invalid header value", Integer.toString(text.getBytes(CHARSET_UTF8).length), mp.getHeaders().getFirst("content-length"));
		Assert.assertEquals("Invalid header value", "xyzw", mp.getHeaders().getFirst("CONTENT-ENCODING"));
		Assert.assertEquals("Invalid header value", "", mp.getHeaders().getFirst("Valid Header-Without Value"));

		// Check effective payload
		Assert.assertArrayEquals("Payloads don't match", text.getBytes(CHARSET_UTF8), decoder.decode(mp));
	}

	@Test
	public void testWithoutHeadersWithCRLFs() throws IOException {
		final StringBuilder payload = new StringBuilder();
		payload.append(NewLine.CRLF);
		payload.append(NewLine.CRLF);
		payload.append(text);

		final byte[] bytes = payload.toString().getBytes(CHARSET_UTF8);
		Assert.assertArrayEquals("Payloads don't match", text.getBytes(CHARSET_UTF8), MqttPayload.fromPayload(bytes).getBody());
	}

	@Test
	public void testDecodeWrongLength() throws IOException {
		final StringBuilder payload = new StringBuilder();
		payload.append("Content-Encoding: identity").append(NewLine.CRLF);
		payload.append("Content-Length: ").append(text.getBytes(CHARSET_UTF8).length + 1 + new Random().nextInt(Byte.MAX_VALUE)).append(NewLine.CRLF);
		payload.append(NewLine.CRLF);
		payload.append(text);

		final byte[] bytes = payload.toString().getBytes(CHARSET_UTF8);
		try {
			decoder.decode(bytes);
			Assert.assertTrue("Content-Length check malfunction", false);
		}
		catch (final IOException e) {
			logger.log(Level.INFO, e.toString(), e);
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testDecodeMissingCRLF() throws IOException {
		final StringBuilder payload = new StringBuilder();
		payload.append("Content-Encoding: identity").append(NewLine.CRLF);
		payload.append("Content-Length: ").append(text.getBytes(CHARSET_UTF8).length + 1 + new Random().nextInt(Byte.MAX_VALUE)).append(NewLine.CRLF);
		payload.append(text);

		final byte[] bytes = payload.toString().getBytes(CHARSET_UTF8);
		try {
			decoder.decode(bytes);
			Assert.assertTrue("CRLFs check malfunction", false);
		}
		catch (final IOException e) {
			logger.log(Level.INFO, e.toString(), e);
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testDecodeNoHeadersEmptyBody() throws IOException {
		final StringBuilder payload = new StringBuilder();
		payload.append(NewLine.CRLF);
		payload.append(NewLine.CRLF);

		final byte[] bytes = payload.toString().getBytes(Charset.forName("UTF-8"));
		Assert.assertArrayEquals("Payloads don't match", new byte[0], decoder.decode(bytes));
	}

	//	private static void log(final byte[] payload) {
	//		System.out.println(Thread.currentThread().getStackTrace()[2]);
	//		final List<byte[]> split = MqttPayload.split(payload);
	//		System.out.println("tokens: " + split.size());
	//
	//		for (final byte[] ba : split) {
	//			System.out.printf("%4d>%s<", ba.length, new String(ba, CHARSET_UTF8));
	//			System.out.println();
	//		}
	//	}

}
