package io.github.albertus82.util.logging;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingSupport {

	public static final String ROOT_LOGGER_NAME = "";

	public static final String DEFAULT_FORMAT = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%tL %4$s %3$s - %5$s%6$s%n";

	private static final String SYSTEM_PROPERTY_FORMAT = "java.util.logging.SimpleFormatter.format";
	private static final String SYSTEM_PROPERTY_CONFIG_CLASS = "java.util.logging.config.class";
	private static final String SYSTEM_PROPERTY_CONFIG_FILE = "java.util.logging.config.file";

	private static final Map<Integer, Level> levels = new TreeMap<Integer, Level>(); // Cache

	private LoggingSupport() {
		throw new IllegalAccessError("Utility class");
	}

	public static Map<Integer, Level> getLevels() {
		if (levels.isEmpty()) { // Lazy init
			levels.put(Level.ALL.intValue(), Level.ALL);
			levels.put(Level.CONFIG.intValue(), Level.CONFIG);
			levels.put(Level.FINE.intValue(), Level.FINE);
			levels.put(Level.FINER.intValue(), Level.FINER);
			levels.put(Level.FINEST.intValue(), Level.FINEST);
			levels.put(Level.INFO.intValue(), Level.INFO);
			levels.put(Level.OFF.intValue(), Level.OFF);
			levels.put(Level.SEVERE.intValue(), Level.SEVERE);
			levels.put(Level.WARNING.intValue(), Level.WARNING);
		}
		return levels;
	}

	public static void setLevel(final Class<?> clazz, final Level level) {
		setLevel(getLoggerName(clazz), level);
	}

	public static void setLevel(final String name, final Level level) {
		final Logger logger = Logger.getLogger(name);
		logger.setLevel(level);
		for (final Handler handler : logger.getHandlers()) {
			handler.setLevel(level);
		}
		if (!ROOT_LOGGER_NAME.equals(name)) {
			for (final Handler handler : getRootHandlers()) {
				handler.setLevel(level);
			}
		}
	}

	public static void setRootLevel(final Level level) {
		setLevel(ROOT_LOGGER_NAME, level);
	}

	public static String getLoggerName(final Class<?> clazz) {
		if (clazz == null) {
			return ROOT_LOGGER_NAME;
		}
		else {
			return clazz.getName();
		}
	}

	public static Handler[] getRootHandlers() {
		return getRootLogger().getHandlers();
	}

	public static Logger getRootLogger() {
		return Logger.getLogger(ROOT_LOGGER_NAME);
	}

	public static String getFormat() {
		return System.getProperty(SYSTEM_PROPERTY_FORMAT);
	}

	public static void setFormat(final String formatString) {
		System.setProperty(SYSTEM_PROPERTY_FORMAT, formatString);
	}

	public static Entry<String, String> getInitialConfigurationProperty() {
		if (System.getProperty(SYSTEM_PROPERTY_CONFIG_CLASS) != null) {
			return new SimpleEntry<String, String>(SYSTEM_PROPERTY_CONFIG_CLASS, System.getProperty(SYSTEM_PROPERTY_CONFIG_CLASS));
		}
		else if (System.getProperty(SYSTEM_PROPERTY_CONFIG_FILE) != null) {
			return new SimpleEntry<String, String>(SYSTEM_PROPERTY_CONFIG_FILE, System.getProperty(SYSTEM_PROPERTY_CONFIG_FILE));
		}
		else {
			return null;
		}
	}

}
