package it.albertus.util.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import it.albertus.util.logging.annotation.ExcludeLoggers;
import it.albertus.util.logging.annotation.LogFormat;

public abstract class AnnotationConfigHandler extends Handler {

	private final String[] exclusions;

	protected AnnotationConfigHandler() {
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
	}

	@Override
	public boolean isLoggable(final LogRecord rec) {
		boolean loggable = super.isLoggable(rec);
		if (!loggable) {
			return false;
		}
		else if (rec.getLoggerName() == null) {
			return true;
		}
		else {
			for (final String exclusion : exclusions) {
				if (rec.getLoggerName().startsWith(exclusion)) {
					return false;
				}
			}
			return true;
		}
	}

}
