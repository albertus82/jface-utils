package it.albertus.util.logging;

import java.util.logging.Logger;

public class LoggerFactory {

	private LoggerFactory() {
		throw new IllegalAccessError("Utility class");
	}

	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(LoggingSupport.getLoggerName(clazz));
	}

	public static Logger getLogger(final String name) {
		return Logger.getLogger(name);
	}

}
