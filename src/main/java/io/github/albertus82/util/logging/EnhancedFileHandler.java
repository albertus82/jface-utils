package io.github.albertus82.util.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnhancedFileHandler extends FileHandler {

	private static final Logger log = LoggerFactory.getLogger(EnhancedFileHandler.class);

	private final String pattern;
	private final int limit;
	private final int count;
	private final boolean append;

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
		this.pattern = config.getPattern();
		this.limit = config.getLimit();
		this.count = config.getCount();
		this.append = config.isAppend();
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
		return pattern;
	}

	/**
	 * Returns the maximum number of bytes to write to any one file.
	 * 
	 * @return the maximum number of bytes to write to any one file
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Returns the number of files to use.
	 * 
	 * @return the number of files to use
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Returns the append mode.
	 * 
	 * @return the append mode
	 */
	public boolean isAppend() {
		return append;
	}

}
