package it.albertus.net.mqtt;

import java.nio.charset.Charset;

public class MqttUtils {

	private MqttUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	static final String HEADER_KEY_CONTENT_ENCODING = "Content-Encoding";
	static final String HEADER_KEY_CONTENT_LENGTH = "Content-Length";
	static final String HEADER_KEY_DATE = "Date";

	static final String HEADER_VALUE_GZIP = "gzip";

}
