package it.albertus.util.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnhancedFileHandler extends FileHandler {

	private static final String FIELD_ACCESS_ERROR_MESSAGE_FORMAT = "Cannot access field %s, try adding the following option to the java command: --add-opens java.logging/java.util.logging=ALL-UNNAMED";

	private static final Logger log = LoggerFactory.getLogger(EnhancedFileHandler.class);

	/**
	 * Initialize a {@code EnhancedFileHandler}.
	 *
	 * @param config the object containing handler configuration
	 * @exception IOException if there are IO problems opening the files.
	 * @exception SecurityException if a security manager exists and if the caller
	 *            does not have <tt>LoggingPermission("control")</tt>.
	 * @exception IllegalArgumentException if limit < 0, or count < 1.
	 * @exception IllegalArgumentException if pattern is an empty string
	 *
	 * @see FileHandler
	 * @see FileHandlerConfig
	 */
	public EnhancedFileHandler(final FileHandlerConfig config) throws IOException {
		super(config.getPattern(), config.getLimit(), config.getCount(), config.isAppend());
		configure(config);
	}

	protected void configure(final FileHandlerConfig config) {
		setLevel(config.getLevel());
		setFilter(config.getFilter());
		setFormatter(config.getFormatter());
		try {
			setEncoding(config.getEncoding());
		}
		catch (final UnsupportedEncodingException e) {
			log.log(Level.WARNING, "Cannot set the character encoding used by this Handler because the named encoding is not supported:", e);
		}
	}

	/**
	 * Returns the pattern for naming the output file.
	 * 
	 * @return the pattern for naming the output file
	 */
	public String getPattern() {
		final String fieldName = "pattern";
		try {
			final Field field = FileHandler.class.getDeclaredField(fieldName);
			field.setAccessible(true); // --add-opens java.logging/java.util.logging=ALL-UNNAMED
			return (String) field.get(this);
		}
		catch (final NoSuchFieldException e) {
			final String message = String.format(FIELD_ACCESS_ERROR_MESSAGE_FORMAT, fieldName);
			log.log(Level.WARNING, message, e);
			throw new LinkageError(message);
		}
		catch (final IllegalAccessException e) {
			final String message = String.format(FIELD_ACCESS_ERROR_MESSAGE_FORMAT, fieldName);
			log.log(Level.WARNING, message, e);
			throw new LinkageError(message);
		}
	}

	/**
	 * Returns the maximum number of bytes to write to any one file.
	 * 
	 * @return the maximum number of bytes to write to any one file
	 */
	public int getLimit() {
		final String fieldName = "limit";
		try {
			final Field field = FileHandler.class.getDeclaredField(fieldName);
			field.setAccessible(true); // --add-opens java.logging/java.util.logging=ALL-UNNAMED
			return (int) field.getLong(this); // limit became "long" since Java 9
		}
		catch (final NoSuchFieldException e) {
			final String message = String.format(FIELD_ACCESS_ERROR_MESSAGE_FORMAT, fieldName);
			log.log(Level.WARNING, message, e);
			throw new LinkageError(message);
		}
		catch (final IllegalAccessException e) {
			final String message = String.format(FIELD_ACCESS_ERROR_MESSAGE_FORMAT, fieldName);
			log.log(Level.WARNING, message, e);
			throw new LinkageError(message);
		}
	}

	/**
	 * Returns the number of files to use.
	 * 
	 * @return the number of files to use
	 */
	public int getCount() {
		final String fieldName = "count";
		try {
			final Field field = FileHandler.class.getDeclaredField(fieldName);
			field.setAccessible(true); // --add-opens java.logging/java.util.logging=ALL-UNNAMED
			return field.getInt(this);
		}
		catch (final NoSuchFieldException e) {
			final String message = String.format(FIELD_ACCESS_ERROR_MESSAGE_FORMAT, fieldName);
			log.log(Level.WARNING, message, e);
			throw new LinkageError(message);
		}
		catch (final IllegalAccessException e) {
			final String message = String.format(FIELD_ACCESS_ERROR_MESSAGE_FORMAT, fieldName);
			log.log(Level.WARNING, message, e);
			throw new LinkageError(message);
		}
	}

	/**
	 * Returns the append mode.
	 * 
	 * @return the append mode
	 */
	public boolean isAppend() {
		final String fieldName = "append";
		try {
			final Field field = FileHandler.class.getDeclaredField(fieldName);
			field.setAccessible(true); // --add-opens java.logging/java.util.logging=ALL-UNNAMED
			return field.getBoolean(this);
		}
		catch (final NoSuchFieldException e) {
			final String message = String.format(FIELD_ACCESS_ERROR_MESSAGE_FORMAT, fieldName);
			log.log(Level.WARNING, message, e);
			throw new LinkageError(message);
		}
		catch (final IllegalAccessException e) {
			final String message = String.format(FIELD_ACCESS_ERROR_MESSAGE_FORMAT, fieldName);
			log.log(Level.WARNING, message, e);
			throw new LinkageError(message);
		}
	}

}
