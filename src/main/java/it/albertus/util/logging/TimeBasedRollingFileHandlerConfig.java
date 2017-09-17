package it.albertus.util.logging;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;

public class TimeBasedRollingFileHandlerConfig extends AbstractFileHandlerConfig {

	static final String DEFAULT_DATE_PATTERN = "yyyyMMdd";

	private String fileNamePattern = "%h/java%d%u.log";
	private String datePattern = DEFAULT_DATE_PATTERN;

	/** Creates a new instance with default values. */
	public TimeBasedRollingFileHandlerConfig() {}

	public static TimeBasedRollingFileHandlerConfig fromHandler(final TimeBasedRollingFileHandler handler) {
		return new TimeBasedRollingFileHandlerConfig(handler.getLevel(), handler.getFilter(), handler.getFormatter(), handler.getEncoding(), handler.getLimit(), handler.getCount(), handler.isAppend(), handler.getFileNamePattern(), handler.getDatePattern());
	}

	public TimeBasedRollingFileHandlerConfig(final Level level, final Filter filter, final Formatter formatter, final String encoding, final int limit, final int count, final boolean append, final String fileNamePattern, final String datePattern) {
		super(level, filter, formatter, encoding, limit, count, append);
		this.fileNamePattern = fileNamePattern;
		this.datePattern = datePattern;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public void setFileNamePattern(final String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(final String datePattern) {
		this.datePattern = datePattern;
	}

}
