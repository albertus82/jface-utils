package it.albertus.util.logging;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.XMLFormatter;

public class FileHandlerBuilder {

	private Level level = Level.ALL; // level specifies the default level for the Handler (defaults to Level.ALL).
	private Filter filter = null; // filter specifies the name of a Filter class to use (defaults to no Filter).
	private Formatter formatter = new XMLFormatter(); // formatter specifies the name of a Formatter class to use (defaults to java.util.logging.XMLFormatter)
	private String encoding = Charset.defaultCharset().name(); // encoding the name of the character set encoding to use (defaults to the default platform encoding).
	private int limit = 0; // limit specifies an approximate maximum amount to write (in bytes) to any one file. If this is zero, then there is no limit. (Defaults to no limit).
	private int count = 1; // count specifies how many output files to cycle through (defaults to 1).
	private String pattern = "%h/java%u.log"; // pattern specifies a pattern for generating the output file name. (Defaults to "%h/java%u.log").
	private boolean append = false; // append specifies whether the FileHandler should append onto any existing files (defaults to false).

	public FileHandlerBuilder level(final Level level) {
		this.level = level;
		return this;
	}

	public FileHandlerBuilder filter(final Filter filter) {
		this.filter = filter;
		return this;
	}

	public FileHandlerBuilder formatter(final Formatter formatter) {
		this.formatter = formatter;
		return this;
	}

	public FileHandlerBuilder encoding(final String encoding) {
		this.encoding = encoding;
		return this;
	}

	public FileHandlerBuilder limit(final int limit) {
		this.limit = limit;
		return this;
	}

	public FileHandlerBuilder count(final int count) {
		this.count = count;
		return this;
	}

	public FileHandlerBuilder pattern(final String pattern) {
		this.pattern = pattern;
		return this;
	}

	public FileHandlerBuilder append(final boolean append) {
		this.append = append;
		return this;
	}

	public FileHandler build() throws IOException {
		final FileHandler fileHandler = new FileHandler(pattern, limit, count, append);
		fileHandler.setLevel(level);
		fileHandler.setFilter(filter);
		fileHandler.setFormatter(formatter);
		fileHandler.setEncoding(encoding);
		return fileHandler;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (append ? 1231 : 1237);
		result = prime * result + count;
		result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + limit;
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FileHandlerBuilder)) {
			return false;
		}
		FileHandlerBuilder other = (FileHandlerBuilder) obj;
		if (append != other.append) {
			return false;
		}
		if (count != other.count) {
			return false;
		}
		if (encoding == null) {
			if (other.encoding != null) {
				return false;
			}
		}
		else if (!encoding.equals(other.encoding)) {
			return false;
		}
		if (level == null) {
			if (other.level != null) {
				return false;
			}
		}
		else if (!level.equals(other.level)) {
			return false;
		}
		if (limit != other.limit) {
			return false;
		}
		if (pattern == null) {
			if (other.pattern != null) {
				return false;
			}
		}
		else if (!pattern.equals(other.pattern)) {
			return false;
		}
		return true;
	}

}
