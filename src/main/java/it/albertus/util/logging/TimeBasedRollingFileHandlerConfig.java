package it.albertus.util.logging;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;

public class TimeBasedRollingFileHandlerConfig extends AbstractFileHandlerConfig {

	static final String DEFAULT_DATE_PATTERN = "yyyyMMdd";

	private String fileNamePattern = "%h/java%d%u.log";
	private String datePattern = DEFAULT_DATE_PATTERN;

	/**
	 * Creates a new instance with default values.
	 * 
	 * @see AbstractFileHandlerConfig
	 */
	public TimeBasedRollingFileHandlerConfig() {}

	public static TimeBasedRollingFileHandlerConfig fromHandler(final TimeBasedRollingFileHandler handler) {
		return new TimeBasedRollingFileHandlerConfig(handler.getLevel(), handler.getFilter(), handler.getFormatter(), handler.getEncoding(), handler.getLimit(), handler.getCount(), handler.isAppend(), handler.getFileNamePattern(), handler.getDatePattern());
	}

	/**
	 * Creates a new instance with the given values.
	 * 
	 * @param level the default level for the Handler
	 * @param filter the name of a Filter class to use
	 * @param formatter the Formatter class to use
	 * @param encoding the name of the character set encoding to use
	 * @param limit an approximate maximum amount to write (in bytes) to any one
	 *        file. If this is zero, then there is no limit
	 * @param count how many output files to cycle through
	 * @param append whether the FileHandler should append onto any existing
	 *        files
	 * @param fileNamePattern the file name pattern
	 * @param datePattern the date pattern used for rolling
	 */
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
