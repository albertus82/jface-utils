package it.albertus.util.logging;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;

public class FileHandlerConfig extends AbstractFileHandlerConfig {

	private String pattern = "%h/java%u.log"; // pattern specifies a pattern for generating the output file name. (Defaults to "%h/java%u.log").

	public FileHandlerConfig() {}

	public static FileHandlerConfig fromHandler(final EnhancedFileHandler handler) {
		return new FileHandlerConfig(handler.getLevel(), handler.getFilter(), handler.getFormatter(), handler.getEncoding(), handler.getLimit(), handler.getCount(), handler.isAppend(), handler.getPattern());
	}

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
