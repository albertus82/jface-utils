package it.albertus.util.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import it.albertus.util.AnnotationConfigurationException;
import it.albertus.util.logging.annotation.ExcludeLoggers;
import it.albertus.util.logging.annotation.LogFilter;
import it.albertus.util.logging.annotation.LogFormat;

public abstract class AnnotationConfigHandler extends Handler {

	private final String[] exclusions;

	public AnnotationConfigHandler() {
		// Exclusions
		final ExcludeLoggers excludeLoggers = getClass().getAnnotation(ExcludeLoggers.class);
		if (excludeLoggers != null && excludeLoggers.value() != null) {
			exclusions = excludeLoggers.value();
		}
		else {
			exclusions = new String[] {};
		}

		// Format
		final LogFormat format = getClass().getAnnotation(LogFormat.class);
		if (format != null && format.value() != null) {
			setFormatter(new CustomFormatter(format.value()));
		}

		// Filter
		final LogFilter filter = getClass().getAnnotation(LogFilter.class);
		if (filter != null && filter.value() != null) {
			try {
				setFilter(filter.value().newInstance());
			}
			catch (final Exception e) {
				throw new AnnotationConfigurationException(e);
			}
		}
	}

	@Override
	public boolean isLoggable(final LogRecord record) {
		boolean loggable = super.isLoggable(record);
		if (!loggable) {
			return false;
		}
		else {
			for (final String exclusion : exclusions) {
				if (record.getLoggerName().startsWith(exclusion)) {
					return false;
				}
			}
			return true;
		}
	}

}
