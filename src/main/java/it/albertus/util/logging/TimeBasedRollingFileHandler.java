package it.albertus.util.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class TimeBasedRollingFileHandler extends Handler {

	public static class Defaults {
		public static final String DATE_PATTERN = "yyyyMMdd";
		public static final int LIMIT = 0;
		public static final int COUNT = 1;
		public static final String FILENAME_PATTERN = "%h/java%d%u.log";
		public static final boolean APPEND = false;

		private Defaults() {
			throw new IllegalAccessError("Constants class");
		}
	}

	private final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(datePattern);
		}
	};

	private String datePattern = Defaults.DATE_PATTERN;
	private int limit = Defaults.LIMIT; // limit specifies an approximate maximum amount to write (in bytes) to any one file. If this is zero, then there is no limit. (Defaults to no limit).
	private int count = Defaults.COUNT; // count specifies how many output files to cycle through (defaults to 1).
	private String fileNamePattern = Defaults.FILENAME_PATTERN; // pattern specifies a pattern for generating the output file name. (Defaults to "%h/java%u.log").
	private boolean append = Defaults.APPEND; // append specifies whether the FileHandler should append onto any existing files (defaults to false).

	private FileHandler underlyingFileHandler;
	private String underlyingFileHandlerPattern;

	public TimeBasedRollingFileHandler() throws IOException {
		final String fileHandlerPattern = getFileHandlerPattern();
		this.underlyingFileHandler = new FileHandler(fileHandlerPattern);
		this.underlyingFileHandlerPattern = fileHandlerPattern;
	}

	public TimeBasedRollingFileHandler(final String datePattern) throws IOException {
		this.datePattern = datePattern;
		final String fileHandlerPattern = getFileHandlerPattern();
		this.underlyingFileHandler = new FileHandler(fileHandlerPattern);
		this.underlyingFileHandlerPattern = fileHandlerPattern;
	}

	public TimeBasedRollingFileHandler(final String datePattern, final String fileNamePattern) throws IOException {
		this.datePattern = datePattern;
		this.fileNamePattern = fileNamePattern;
		final String fileHandlerPattern = getFileHandlerPattern();
		this.underlyingFileHandler = new FileHandler(fileHandlerPattern);
		this.underlyingFileHandlerPattern = fileHandlerPattern;
	}

	public TimeBasedRollingFileHandler(final String datePattern, final String fileNamePattern, final boolean append) throws IOException {
		this.datePattern = datePattern;
		this.fileNamePattern = fileNamePattern;
		this.append = append;
		final String fileHandlerPattern = getFileHandlerPattern();
		this.underlyingFileHandler = new FileHandler(fileHandlerPattern, append);
		this.underlyingFileHandlerPattern = fileHandlerPattern;
	}

	public TimeBasedRollingFileHandler(final String datePattern, final String fileNamePattern, final int limit, final int count) throws IOException {
		this.datePattern = datePattern;
		this.fileNamePattern = fileNamePattern;
		this.limit = limit;
		this.count = count;
		final String fileHandlerPattern = getFileHandlerPattern();
		this.underlyingFileHandler = new FileHandler(fileHandlerPattern, limit, count);
		this.underlyingFileHandlerPattern = fileHandlerPattern;
	}

	public TimeBasedRollingFileHandler(final String datePattern, final String fileNamePattern, final int limit, final int count, final boolean append) throws IOException {
		this.datePattern = datePattern;
		this.fileNamePattern = fileNamePattern;
		this.limit = limit;
		this.count = count;
		this.append = append;
		final String fileHandlerPattern = getFileHandlerPattern();
		this.underlyingFileHandler = new FileHandler(fileHandlerPattern, limit, count, append);
		this.underlyingFileHandlerPattern = fileHandlerPattern;
	}

	@Override
	public synchronized void publish(final LogRecord record) {
		if (!isLoggable(record)) {
			return;
		}

		final String fileHandlerPattern = getFileHandlerPattern();
		if (!fileHandlerPattern.equals(underlyingFileHandlerPattern)) {
			try {
				final FileHandler oldFileHandler = underlyingFileHandler;
				final FileHandler newFileHandler = new FileHandler(fileHandlerPattern, limit, count, append);
				newFileHandler.setFormatter(oldFileHandler.getFormatter());
				newFileHandler.setLevel(oldFileHandler.getLevel());
				newFileHandler.setEncoding(oldFileHandler.getEncoding());
				newFileHandler.setErrorManager(oldFileHandler.getErrorManager());
				newFileHandler.setFilter(oldFileHandler.getFilter());
				underlyingFileHandler = newFileHandler;
				underlyingFileHandlerPattern = fileHandlerPattern;
				oldFileHandler.close();
			}
			catch (final IOException e) {
				reportError(e.toString(), e, ErrorManager.OPEN_FAILURE);
			}
		}

		underlyingFileHandler.publish(record);
	}

	protected String getFileHandlerPattern() {
		if (!fileNamePattern.contains("%d")) {
			throw new IllegalArgumentException("fileNamePattern must contain \"%d\"");
		}
		return fileNamePattern.replace("%d", dateFormat.get().format(new Date()));
	}

	@Override
	public void flush() {
		underlyingFileHandler.flush();
	}

	@Override
	public void close() {
		underlyingFileHandler.close();
	}

	@Override
	public void setFormatter(final Formatter newFormatter) {
		underlyingFileHandler.setFormatter(newFormatter);
	}

	@Override
	public Formatter getFormatter() {
		return underlyingFileHandler.getFormatter();
	}

	@Override
	public void setEncoding(final String encoding) throws UnsupportedEncodingException {
		underlyingFileHandler.setEncoding(encoding);
	}

	@Override
	public String getEncoding() {
		return underlyingFileHandler.getEncoding();
	}

	@Override
	public void setFilter(final Filter newFilter) {
		underlyingFileHandler.setFilter(newFilter);
	}

	@Override
	public Filter getFilter() {
		return underlyingFileHandler.getFilter();
	}

	@Override
	public void setErrorManager(final ErrorManager em) {
		underlyingFileHandler.setErrorManager(em);
	}

	@Override
	public ErrorManager getErrorManager() {
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

	public String getDatePattern() {
		return datePattern;
	}

	public int getLimit() {
		return limit;
	}

	public int getCount() {
		return count;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public boolean isAppend() {
		return append;
	}

}
