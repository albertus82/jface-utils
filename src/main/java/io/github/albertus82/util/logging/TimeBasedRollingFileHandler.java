package io.github.albertus82.util.logging;

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

import io.github.albertus82.util.ISupplier;

public class TimeBasedRollingFileHandler extends Handler {

	private final String fileNamePattern;
	private final String datePattern;
	private final DateFormat dateFormat;
	private final ISupplier<Date> dateSupplier;

	private EnhancedFileHandler underlyingFileHandler;

	public TimeBasedRollingFileHandler(final TimeBasedRollingFileHandlerConfig config) throws IOException {
		this(config, new ISupplier<Date>() {
			@Override
			public Date get() {
				return new Date();
			}
		});
	}

	TimeBasedRollingFileHandler(final TimeBasedRollingFileHandlerConfig config, final ISupplier<Date> dateSupplier) throws IOException {
		this.fileNamePattern = config.getFileNamePattern();
		this.datePattern = config.getDatePattern();
		this.dateFormat = new SimpleDateFormat(datePattern);
		this.dateSupplier = dateSupplier;

		final FileHandlerConfig underlyingFileHandlerConfig = new FileHandlerConfig(config.getLevel(), config.getFilter(), config.getFormatter(), config.getEncoding(), config.getLimit(), config.getCount(), config.isAppend(), generateFileHandlerPattern(config.getFileNamePattern(), dateFormat, dateSupplier));
		underlyingFileHandler = new EnhancedFileHandler(underlyingFileHandlerConfig);
	}

	@Override
	public synchronized void publish(final LogRecord rec) {
		if (!isLoggable(rec)) {
			return;
		}

		final String fileHandlerPattern = generateFileHandlerPattern(fileNamePattern, dateFormat, dateSupplier);
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
				reportError(null, e, ErrorManager.OPEN_FAILURE);
			}
		}

		underlyingFileHandler.publish(rec);
	}

	private static String generateFileHandlerPattern(final String fileNamePattern, final DateFormat dateFormat, final ISupplier<Date> dateSupplier) {
		if (!fileNamePattern.contains("%d")) {
			throw new IllegalArgumentException("fileNamePattern must contain \"%d\"");
		}
		return fileNamePattern.replace("%d", dateFormat.format(dateSupplier.get()));
	}

	@Override
	public void flush() {
		underlyingFileHandler.flush();
	}

	@Override
	public void close() {
		underlyingFileHandler.close();
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
