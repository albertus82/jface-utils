package it.albertus.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {

	private static final String VERSION_FILE_NAME = "version.properties";
	private static final String KEY_VERSION_NUMBER = "version.number";
	private static final String KEY_VERSION_DATE = "version.date";

	private final Properties properties = new Properties();
	private final String fileName;

	// Lazy initialization...
	private static class Singleton {
		private static final Version version = new Version();
	}
	
	public static Version getInstance() {
		return Singleton.version;
	}

	protected Version() {
		this(VERSION_FILE_NAME);
	}

	protected Version(String fileName) {
		this.fileName = fileName;
		try {
			load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void load() throws IOException {
		final InputStream inputStream = getClass().getResourceAsStream('/' + fileName);
		if (inputStream != null) {
			properties.load(inputStream);
			inputStream.close();
		}
	}

	public String getNumber() {
		return properties.getProperty(KEY_VERSION_NUMBER);
	}

	public String getDate() {
		return properties.getProperty(KEY_VERSION_DATE);
	}

}
