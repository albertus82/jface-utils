package io.github.albertus82.net.mqtt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MqttUtils {

	private MqttUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

	static final String HEADER_KEY_CONTENT_ENCODING = "Content-Encoding";
	static final String HEADER_KEY_CONTENT_LENGTH = "Content-Length";
	static final String HEADER_KEY_DATE = "Date";

	static final String HEADER_VALUE_GZIP = "gzip";

}
