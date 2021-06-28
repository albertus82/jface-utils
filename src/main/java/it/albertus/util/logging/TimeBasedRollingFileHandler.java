package it.albertus.util.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class TimeBasedRollingFileHandler extends Handler {

	private final ThreadLocal<DateFormat> dateFormats = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(datePattern);
		}
	};

	private final String fileNamePattern;
	private final String datePattern;

	private EnhancedFileHandler underlyingFileHandler;

	public TimeBasedRollingFileHandler(final TimeBasedRollingFileHandlerConfig config) throws IOException {
		fileNamePattern = config.getFileNamePattern();
		datePattern = config.getDatePattern();

		final FileHandlerConfig underlyingFileHandlerConfig = new FileHandlerConfig(config.getLevel(), config.getFilter(), config.getFormatter(), config.getEncoding(), config.getLimit(), config.getCount(), config.isAppend(), generateFileHandlerPattern(config.getFileNamePattern(), dateFormats.get()));
		underlyingFileHandler = new EnhancedFileHandler(underlyingFileHandlerConfig);
	}

	@Override
	public synchronized void publish(final LogRecord rec) {
		if (!isLoggable(rec)) {
			return;
		}

		final String fileHandlerPattern = generateFileHandlerPattern(fileNamePattern, dateFormats.get());
		if (!fileHandlerPattern.equals(underlyingFileHandler.getPattern())) { // check if date has changed
			try {
				final EnhancedFileHandler oldFileHandler = underlyingFileHandler; // must be closed at the end!

				final FileHandlerConfig newFileHandlerConfig = FileHandlerConfig.fromHandler(oldFileHandler);
				newFileHandlerConfig.setPattern(fileHandlerPattern); // Update the file name pattern with the new date

				final EnhancedFileHandler newFileHandler = new EnhancedFileHandler(newFileHandlerConfig);
				newFileHandler.setErrorManager(oldFileHandler.getErrorManager());

				// Switch to the new handler and close the old one.
				underlyingFileHandler = newFileHandler;
				oldFileHandler.close();
			}
			catch (final IOException e) {
				reportError(e.toString(), e, ErrorManager.OPEN_FAILURE);
			}
		}

		underlyingFileHandler.publish(rec);
	}

	private static String generateFileHandlerPattern(final String fileNamePattern, final DateFormat dateFormat) {
		if (!fileNamePattern.contains("%d")) {
			throw new IllegalArgumentException("fileNamePattern must contain \"%d\"");
		}
		return fileNamePattern.replace("%d", dateFormat.format(new Date()));
	}

	@Override
	public void flush() {
		underlyingFileHandler.flush();
	}

	@Override
	public void close() {
		underlyingFileHandler.close();
		dateFormats.remove(); // Not a solution but better than nothing
	}

	public String getDatePattern() {
		return datePattern;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	@Override
	public synchronized void setFormatter(final Formatter newFormatter) {
		underlyingFileHandler.setFormatter(newFormatter);
	}

	@Override
	public synchronized Formatter getFormatter() {
		return underlyingFileHandler.getFormatter();
	}

	@Override
	public synchronized void setEncoding(final String encoding) throws UnsupportedEncodingException {
		underlyingFileHandler.setEncoding(encoding);
	}

	@Override
	public synchronized String getEncoding() {
		return underlyingFileHandler.getEncoding();
	}

	@Override
	public synchronized void setFilter(final Filter newFilter) {
		underlyingFileHandler.setFilter(newFilter);
	}

	@Override
	public synchronized Filter getFilter() {
		return underlyingFileHandler.getFilter();
	}

	@Override
	public synchronized void setErrorManager(final ErrorManager em) {
		underlyingFileHandler.setErrorManager(em);
	}

	@Override
	public synchronized ErrorManager getErrorManager() {
		return underlyingFileHandler.getErrorManager();
	}

	@Override
	public synchronized void setLevel(final Level newLevel) {
		underlyingFileHandler.setLevel(newLevel);
	}

	@Override
	public synchronized Level getLevel() {
		return underlyingFileHandler.getLevel();
	}

	public int getLimit() {
		return underlyingFileHandler.getLimit();
	}

	public int getCount() {
		return underlyingFileHandler.getCount();
	}

	public boolean isAppend() {
		return underlyingFileHandler.isAppend();
	}

}
