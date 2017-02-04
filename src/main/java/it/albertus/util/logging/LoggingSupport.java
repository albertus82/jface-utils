package it.albertus.util.logging;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingSupport {

	public static final String ROOT_LOGGER_NAME = "";

	private static final Map<Integer, Level> levels = new TreeMap<Integer, Level>();

	static {
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

	private LoggingSupport() {
		throw new IllegalAccessError("Utility class");
	}

	public static Map<Integer, Level> getLevels() {
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
		if (clazz == null || clazz.getPackage() == null) {
			return ROOT_LOGGER_NAME;
		}
		else {
			return clazz.getPackage().getName(); // package-based
		}
	}

	public static Handler[] getRootHandlers() {
		return getRootLogger().getHandlers();
	}

	public static Logger getRootLogger() {
		return Logger.getLogger(ROOT_LOGGER_NAME);
	}

}
