package it.albertus.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import javax.activation.MimetypesFileTypeMap;

import it.albertus.util.IOUtils;
import it.albertus.util.logging.LoggerFactory;

public class MimeTypes {

	private static final String MIME_TYPES_RESOURCE_NAME = "mime-types.properties";

	private static final Properties contentTypes;

	static {
		contentTypes = new Properties();
		InputStream is = null;
		try {
			is = MimeTypes.class.getResourceAsStream(MIME_TYPES_RESOURCE_NAME);
			contentTypes.load(is);
		}
		catch (final IOException e) {
			LoggerFactory.getLogger(MimeTypes.class).log(Level.WARNING, "Unable to load resource " + MIME_TYPES_RESOURCE_NAME, e);
		}
		finally {
			IOUtils.closeQuietly(is);
		}
	}

	private MimeTypes() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Tries to determine the <b>media type</b> (aka <b>content type</b> or
	 * <b>MIME type</b>) for the provided file name.
	 * 
	 * @param fileName the file name
	 * @return the computed media type
	 * @see #getContentTypes()
	 * @see MimetypesFileTypeMap#getContentType(String)
	 */
	public static String getContentType(final String fileName) {
		final String extension = fileName.indexOf('.') != -1 ? fileName.substring(fileName.lastIndexOf('.') + 1).trim().toLowerCase() : null;
		String contentType = null;
		if (extension != null && !extension.isEmpty()) {
			contentType = contentTypes.getProperty(extension);
		}
		if (contentType == null) {
			contentType = new MimetypesFileTypeMap().getContentType(fileName);
		}
		return contentType.trim();
	}

	public static Properties getContentTypes() {
		return contentTypes;
	}

}
