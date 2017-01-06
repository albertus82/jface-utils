package it.albertus.util.logging;

import java.util.logging.Logger;

public class LoggerFactory {

	private LoggerFactory() {
		throw new IllegalAccessError("Utility class");
	}

	public static Logger getLogger(final Class<?> clazz) {
		return Logger.getLogger(clazz.getPackage().getName()); // package-based
	}

}
