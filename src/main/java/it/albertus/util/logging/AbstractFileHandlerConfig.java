package it.albertus.util.logging;

import java.nio.charset.Charset;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.XMLFormatter;

public abstract class AbstractFileHandlerConfig {

	/**
	 * Specifies the default level for the Handler (defaults to {@code Level.ALL}).
	 */
	private Level level = Level.ALL;

	/** Specifies the name of a Filter class to use (defaults to no Filter). */
	private Filter filter = null;

	/**
	 * Specifies the name of a Formatter class to use (defaults to
	 * {@link java.util.logging.XMLFormatter}).
	 */
	private Formatter formatter = new XMLFormatter();

	/**
	 * Specifies the name of the character set encoding to use (defaults to the
	 * default platform encoding).
	 */
	private String encoding = Charset.defaultCharset().name();

	/**
	 * Specifies an approximate maximum amount to write (in bytes) to any one file.
	 * If this is zero, then there is no limit. (Defaults to no limit).
	 */
	private int limit = 0;

	/**
	 * Specifies how many output files to cycle through (defaults to {@code 1}).
	 */
	private int count = 1;

	/**
	 * Specifies whether the FileHandler should append onto any existing files
	 * (defaults to {@code false}).
	 */
	private boolean append = false;

	/** Creates a new instance with default values. */
	protected AbstractFileHandlerConfig() {}

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
	 */
	protected AbstractFileHandlerConfig(final Level level, final Filter filter, final Formatter formatter, final String encoding, final int limit, final int count, final boolean append) {
		this.level = level;
		this.filter = filter;
		this.formatter = formatter;
		this.encoding = encoding;
		this.limit = limit;
		this.count = count;
		this.append = append;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(final Level level) {
		this.level = level;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(final Filter filter) {
		this.filter = filter;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public void setFormatter(final Formatter formatter) {
		this.formatter = formatter;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(final int limit) {
		this.limit = limit;
	}

	public int getCount() {
		return count;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(final boolean append) {
		this.append = append;
	}

}
