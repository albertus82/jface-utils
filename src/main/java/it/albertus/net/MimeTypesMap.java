package it.albertus.net;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Nullable;

import it.albertus.util.IOUtils;

/**
 * Singleton decorator for {@link MimetypesFileTypeMap} that uses the <b>Apache
 * <tt>httpd</tt></b> <tt>mime.types</tt> file.
 */
public class MimeTypesMap {

	private static final String MIME_TYPES_RESOURCE_NAME = "mime.types";

	private static @Nullable MimeTypesMap instance;

	/**
	 * Returns a singleton instance of this class.
	 * 
	 * @return the singleton.
	 */
	public static synchronized MimeTypesMap getInstance() {
		if (instance == null) {
			instance = new MimeTypesMap();
		}
		return instance;
	}

	private final FileTypeMap map;

	private MimeTypesMap() {
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream(MIME_TYPES_RESOURCE_NAME);
			map = is != null ? new MimetypesFileTypeMap(is) : new MimetypesFileTypeMap();
		}
		finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Return the MIME type of the file object. The implementation in this class
	 * calls <code>getContentType(f.getName())</code>.
	 *
	 * @param f the file
	 * @return the file's MIME type
	 */
	public String getContentType(final File f) {
		return getContentType(f.getName());
	}

	/**
	 * Return the MIME type based on the specified file name. If no entry is found,
	 * the type "application/octet-stream" is returned.
	 *
	 * @param filename the file name
	 * @return the file's MIME type
	 */
	public String getContentType(final String filename) {
		return map.getContentType(filename.trim().toLowerCase(Locale.ROOT));
	}

}
