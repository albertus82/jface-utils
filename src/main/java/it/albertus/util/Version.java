package it.albertus.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Version {

	private static final String VERSION_FILE_NAME = "version.properties";
	private static final String KEY_VERSION_NUMBER = "version.number";
	private static final String KEY_VERSION_DATE = "version.date";
	private static final String ISO_8601_PATTERN = "yyyy-MM-dd";

	private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(ISO_8601_PATTERN);
		}
	};

	private final Properties properties = new Properties();
	private final String fileName;

	// Lazy initialization...
	private static class Singleton {
		private static final Version instance = new Version();
	}

	public static Version getInstance() {
		return Singleton.instance;
	}

	protected Version() {
		this(VERSION_FILE_NAME);
	}

	protected Version(final String fileName) {
		this.fileName = fileName;
		try {
			load();
		}
		catch (final IOException e) {
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

	public Date parseDate() throws IllegalArgumentException {
		try {
			return dateFormat.get().parse(getDate());
		}
		catch (final ParseException pe) {
			throw new IllegalArgumentException(pe);
		}
	}

	@Override
	public String toString() {
		return properties.toString();
	}

}
