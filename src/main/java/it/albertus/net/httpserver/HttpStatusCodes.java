package it.albertus.net.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;

import it.albertus.util.IOUtils;
import it.albertus.util.MapUtils;
import it.albertus.util.logging.LoggerFactory;

public class HttpStatusCodes {

	private static final String HTTP_CODES_RESOURCE_NAME = "http-codes.properties";

	private static final Map<Integer, String> map;

	private HttpStatusCodes() {
		throw new IllegalAccessError("Utility class");
	}

	static {
		final Properties properties = new Properties();
		InputStream is = null;
		try {
			is = HttpStatusCodes.class.getResourceAsStream(HTTP_CODES_RESOURCE_NAME);
			properties.load(is);
		}
		catch (final IOException e) {
			LoggerFactory.getLogger(HttpStatusCodes.class).log(Level.WARNING, "Unable to load resource " + HTTP_CODES_RESOURCE_NAME, e);
		}
		finally {
			IOUtils.closeQuietly(is);
		}
		map = MapUtils.<Integer, String> newHashMapWithExpectedSize(properties.size());
		for (final Entry<?, ?> entry : properties.entrySet()) {
			map.put(Integer.valueOf(entry.getKey().toString()), entry.getValue().toString());
		}
	}

	/**
	 * Returns the map containing all the known HTTP status codes with their
	 * names.
	 * 
	 * @return the map containing the HTTP status codes
	 */
	public static Map<Integer, String> getMap() {
		return map;
	}

}
