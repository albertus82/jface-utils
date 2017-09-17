package it.albertus.util.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnhancedFileHandler extends FileHandler {

	private static final String FIELD_ACCESS_ERROR_MESSAGE = "Cannot access field";

	private static final Logger logger = LoggerFactory.getLogger(EnhancedFileHandler.class);

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
			logger.log(Level.WARNING, e.toString(), e);
		}
	}

	public String getPattern() {
		final String fieldName = "pattern";
		try {
			final Field patternField = FileHandler.class.getDeclaredField(fieldName);
			patternField.setAccessible(true);
			return (String) patternField.get(this);
		}
		catch (final NoSuchFieldException e) {
			logger.log(Level.WARNING, FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName, e);
			throw new LinkageError(FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName);
		}
		catch (final IllegalAccessException e) {
			logger.log(Level.WARNING, FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName, e);
			throw new LinkageError(FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName);
		}
	}

	public int getLimit() {
		final String fieldName = "limit";
		try {
			final Field patternField = FileHandler.class.getDeclaredField(fieldName);
			patternField.setAccessible(true);
			return patternField.getInt(this);
		}
		catch (final NoSuchFieldException e) {
			logger.log(Level.WARNING, FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName, e);
			throw new LinkageError(FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName);
		}
		catch (final IllegalAccessException e) {
			logger.log(Level.WARNING, FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName, e);
			throw new LinkageError(FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName);
		}
	}

	public int getCount() {
		final String fieldName = "count";
		try {
			final Field patternField = FileHandler.class.getDeclaredField(fieldName);
			patternField.setAccessible(true);
			return patternField.getInt(this);
		}
		catch (final NoSuchFieldException e) {
			logger.log(Level.WARNING, FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName, e);
			throw new LinkageError(FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName);
		}
		catch (final IllegalAccessException e) {
			logger.log(Level.WARNING, FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName, e);
			throw new LinkageError(FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName);
		}
	}

	public boolean isAppend() {
		final String fieldName = "append";
		try {
			final Field patternField = FileHandler.class.getDeclaredField(fieldName);
			patternField.setAccessible(true);
			return patternField.getBoolean(this);
		}
		catch (final NoSuchFieldException e) {
			logger.log(Level.WARNING, FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName, e);
			throw new LinkageError(FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName);
		}
		catch (final IllegalAccessException e) {
			logger.log(Level.WARNING, FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName, e);
			throw new LinkageError(FIELD_ACCESS_ERROR_MESSAGE + ' ' + fieldName);
		}
	}

}
