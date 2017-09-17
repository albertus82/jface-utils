package it.albertus.util.logging;

import java.nio.charset.Charset;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.XMLFormatter;

public abstract class AbstractFileHandlerConfig {

	private Level level = Level.ALL; // level specifies the default level for the Handler (defaults to Level.ALL).
	private Filter filter = null; // filter specifies the name of a Filter class to use (defaults to no Filter).
	private Formatter formatter = new XMLFormatter(); // formatter specifies the name of a Formatter class to use (defaults to java.util.logging.XMLFormatter)
	private String encoding = Charset.defaultCharset().name(); // encoding the name of the character set encoding to use (defaults to the default platform encoding).
	private int limit = 0; // limit specifies an approximate maximum amount to write (in bytes) to any one file. If this is zero, then there is no limit. (Defaults to no limit).
	private int count = 1; // count specifies how many output files to cycle through (defaults to 1).
	private boolean append = false; // append specifies whether the FileHandler should append onto any existing files (defaults to false).

	protected AbstractFileHandlerConfig() {}

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
