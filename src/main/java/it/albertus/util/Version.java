package it.albertus.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.jface.JFaceMessages;
import it.albertus.util.logging.LoggerFactory;

/**
 * Reads the {@value Version#VERSION_FILE_NAME} file from the class path root or
 * the {@code META-INF} directory. The read values can be retrieved using
 * {@link #getNumber()} and {@link #getDate()}.
 */
public class Version {

	private static final Logger logger = LoggerFactory.getLogger(Version.class);

	public static final String ISO_8601_PATTERN = "yyyy-MM-dd";

	private static final String VERSION_FILE_NAME = "version.properties";
	private static final String KEY_VERSION_NUMBER = "version.number";
	private static final String KEY_VERSION_DATE = "version.date";

	private static final Properties properties = new Properties();

	static {
		InputStream in = null;
		String resourceName = '/' + VERSION_FILE_NAME;
		try {
			in = Version.class.getResourceAsStream(resourceName);
			if (in != null) {
				properties.load(in);
			}
			else {
				resourceName = "/META-INF/" + VERSION_FILE_NAME;
				in = Version.class.getResourceAsStream(resourceName);
				if (in != null) {
					properties.load(in);
				}
			}
		}
		catch (final IOException e) {
			logger.log(Level.WARNING, JFaceMessages.get("err.load.file", resourceName), e);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (final IOException e) {
					logger.log(Level.FINE, "Cannot close resource \"" + resourceName + "\".", e);
				}
			}
		}
	}

	/**
	 * Return the version number or {@code null} if not present.
	 * 
	 * @return the version number as {@link String}.
	 */
	public static String getNumber() {
		return properties.getProperty(KEY_VERSION_NUMBER);
	}

	/**
	 * Return the version date or {@code null} if not present.
	 * 
	 * @return the version date.
	 * 
	 * @throws ParseException if the {@value #KEY_VERSION_DATE} property is not a
	 *                        date in the {@value #ISO_8601_PATTERN} format.
	 */
	public static Date getDate() throws ParseException {
		final String property = properties.getProperty(KEY_VERSION_DATE);
		if (property != null) {
			return new SimpleDateFormat(ISO_8601_PATTERN).parse(property);
		}
		else {
			return null;
		}
	}

	private Version() {
		throw new IllegalAccessError();
	}

}
