package io.github.albertus82.util.logging;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;

/**
 * Holds the configuration parameters for {@code EnhancedFileHandler}.
 * 
 * @see EnhancedFileHandler
 */
public class FileHandlerConfig extends AbstractFileHandlerConfig {

	/**
	 * Specifies a pattern for generating the output file name. (Defaults to
	 * {@code %h/java%u.log}).
	 */
	private String pattern = "%h/java%u.log";

	/**
	 * Creates a new instance with default values.
	 * 
	 * @see AbstractFileHandlerConfig
	 */
	public FileHandlerConfig() {}

	public static FileHandlerConfig fromHandler(final EnhancedFileHandler handler) {
		return new FileHandlerConfig(handler.getLevel(), handler.getFilter(), handler.getFormatter(), handler.getEncoding(), handler.getLimit(), handler.getCount(), handler.isAppend(), handler.getPattern());
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
	 * @param append whether the FileHandler should append onto any existing files
	 * @param pattern the file name pattern
	 */
	public FileHandlerConfig(final Level level, final Filter filter, final Formatter formatter, final String encoding, final int limit, final int count, final boolean append, final String pattern) {
		super(level, filter, formatter, encoding, limit, count, append);
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

}
