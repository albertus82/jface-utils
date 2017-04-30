package it.albertus.util.logging;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.XMLFormatter;

public class TimeBasedRollingFileHandlerBuilder {

	private String datePattern = TimeBasedRollingFileHandler.DEFAULT_DATE_PATTERN;
	private Level level = Level.ALL; // level specifies the default level for the Handler (defaults to Level.ALL).
	private Filter filter = null; // filter specifies the name of a Filter class to use (defaults to no Filter).
	private Formatter formatter = new XMLFormatter(); // formatter specifies the name of a Formatter class to use (defaults to java.util.logging.XMLFormatter)
	private String encoding = Charset.defaultCharset().name(); // encoding the name of the character set encoding to use (defaults to the default platform encoding).
	private int limit = TimeBasedRollingFileHandler.DEFAULT_LIMIT; // limit specifies an approximate maximum amount to write (in bytes) to any one file. If this is zero, then there is no limit. (Defaults to no limit).
	private int count = TimeBasedRollingFileHandler.DEFAULT_COUNT; // count specifies how many output files to cycle through (defaults to 1).
	private String fileNamePattern = TimeBasedRollingFileHandler.DEFAULT_FILENAME_PATTERN; // pattern specifies a pattern for generating the output file name. (Defaults to "%h/java%u.log").
	private boolean append = TimeBasedRollingFileHandler.DEFAULT_APPEND; // append specifies whether the FileHandler should append onto any existing files (defaults to false).

	public TimeBasedRollingFileHandlerBuilder datePattern(final String datePattern) {
		this.datePattern = datePattern;
		return this;
	}

	public TimeBasedRollingFileHandlerBuilder level(final Level level) {
		this.level = level;
		return this;
	}

	public TimeBasedRollingFileHandlerBuilder filter(final Filter filter) {
		this.filter = filter;
		return this;
	}

	public TimeBasedRollingFileHandlerBuilder formatter(final Formatter formatter) {
		this.formatter = formatter;
		return this;
	}

	public TimeBasedRollingFileHandlerBuilder encoding(final String encoding) {
		this.encoding = encoding;
		return this;
	}

	public TimeBasedRollingFileHandlerBuilder limit(final int limit) {
		this.limit = limit;
		return this;
	}

	public TimeBasedRollingFileHandlerBuilder count(final int count) {
		this.count = count;
		return this;
	}

	public TimeBasedRollingFileHandlerBuilder fileNamePattern(final String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
		return this;
	}

	public TimeBasedRollingFileHandlerBuilder append(final boolean append) {
		this.append = append;
		return this;
	}

	public TimeBasedRollingFileHandler build() throws IOException {
		final TimeBasedRollingFileHandler fileHandler = new TimeBasedRollingFileHandler(datePattern, fileNamePattern, limit, count, append);
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
		result = prime * result + ((datePattern == null) ? 0 : datePattern.hashCode());
		result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result + ((fileNamePattern == null) ? 0 : fileNamePattern.hashCode());
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + limit;
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
		if (!(obj instanceof TimeBasedRollingFileHandlerBuilder)) {
			return false;
		}
		TimeBasedRollingFileHandlerBuilder other = (TimeBasedRollingFileHandlerBuilder) obj;
		if (append != other.append) {
			return false;
		}
		if (count != other.count) {
			return false;
		}
		if (datePattern == null) {
			if (other.datePattern != null) {
				return false;
			}
		}
		else if (!datePattern.equals(other.datePattern)) {
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
		if (fileNamePattern == null) {
			if (other.fileNamePattern != null) {
				return false;
			}
		}
		else if (!fileNamePattern.equals(other.fileNamePattern)) {
			return false;
		}
		if (filter == null) {
			if (other.filter != null) {
				return false;
			}
		}
		else if (!filter.equals(other.filter)) {
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
		return true;
	}

}
