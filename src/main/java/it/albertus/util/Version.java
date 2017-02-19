package it.albertus.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.jface.JFaceMessages;
import it.albertus.util.logging.LoggerFactory;

public class Version {

	private static final Logger logger = LoggerFactory.getLogger(Version.class);

	public static final String ISO_8601_PATTERN = "yyyy-MM-dd";

	private static final String VERSION_FILE_NAME = "version.properties";
	private static final String KEY_VERSION_NUMBER = "version.number";
	private static final String KEY_VERSION_DATE = "version.date";

	private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(ISO_8601_PATTERN);
		}
	};

	private final Properties properties = new Properties();
	private final String fileName;

	protected Version() {
		this(VERSION_FILE_NAME);
	}

	protected Version(final String fileName) {
		this.fileName = fileName;
		try {
			load();
		}
		catch (final IOException e) {
			logger.log(Level.WARNING, JFaceMessages.get("err.load.file", fileName), e);
		}
	}

	// Lazy initialization...
	private static class Singleton {
		private static final Version instance = new Version();

		private Singleton() {
			throw new IllegalAccessError();
		}
	}

	public static Version getInstance() {
		return Singleton.instance;
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

	public Date getDate() throws IllegalArgumentException {
		try {
			return dateFormat.get().parse(properties.getProperty(KEY_VERSION_DATE));
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
