package it.albertus.util.logging;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import it.albertus.util.logging.annotation.FilterExclusions;

public abstract class LoggerNameFilter implements Filter {

	@Override
	public boolean isLoggable(final LogRecord record) {
		return !isExcluded(record);
	}

	protected boolean isExcluded(final LogRecord record) {
		for (final String exclusion : getExclusions()) {
			if (record.getLoggerName().startsWith(exclusion)) {
				return true;
			}
		}
		return false;
	}

	public String[] getExclusions() {
		final FilterExclusions exclusions = this.getClass().getAnnotation(FilterExclusions.class);
		return exclusions != null && exclusions.names() != null ? exclusions.names() : new String[] {};
	}

}
