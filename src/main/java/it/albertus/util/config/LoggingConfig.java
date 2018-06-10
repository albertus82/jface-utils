package it.albertus.util.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.util.logging.CustomFormatter;
import it.albertus.util.logging.EnhancedFileHandler;
import it.albertus.util.logging.FileHandlerConfig;
import it.albertus.util.logging.LoggerFactory;
import it.albertus.util.logging.LoggingSupport;

public abstract class LoggingConfig extends Configuration {

	public static final String DEFAULT_LOG_FORMAT_FILE = "%1$td/%1$tm/%1$tY %1$tH:%1$tM:%1$tS.%tL %4$s %3$s - %5$s%6$s%n";
	public static final Level DEFAULT_LOGGING_LEVEL = Level.INFO;
	public static final boolean DEFAULT_LOGGING_FILES_ENABLED = true;
	public static final int DEFAULT_LOGGING_FILES_LIMIT = 1024;
	public static final int DEFAULT_LOGGING_FILES_COUNT = 5;

	private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

	private EnhancedFileHandler fileHandler;

	public LoggingConfig(final String cfgFileName, final boolean prependOsSpecificConfigurationDir) throws IOException {
		super(new PropertiesConfiguration(cfgFileName, prependOsSpecificConfigurationDir));
	}

	protected void init() {
		updateLogging();
	}

	@Override
	public void reload() throws IOException {
		super.reload();
		init();
	}

	protected void updateLogging() {
		if (LoggingSupport.getInitialConfigurationProperty() == null) {
			updateLoggingLevel();

			if (isFileHandlerEnabled()) {
				enableLoggingFileHandler();
			}
			else {
				disableLoggingFileHandler();
			}
		}
	}

	protected void enableLoggingFileHandler() {
		final String fileHandlerPattern = getFileHandlerPattern();
		if (fileHandlerPattern != null && !fileHandlerPattern.isEmpty()) {
			final FileHandlerConfig newConfig = new FileHandlerConfig();
			newConfig.setPattern(fileHandlerPattern);
			newConfig.setLimit(getFileHandlerLimit());
			newConfig.setCount(getFileHandlerCount());
			newConfig.setAppend(true);
			newConfig.setFormatter(new CustomFormatter(getFileHandlerFormat()));

			if (fileHandler != null) {
				final FileHandlerConfig oldConfig = FileHandlerConfig.fromHandler(fileHandler);
				if (!oldConfig.getPattern().equals(newConfig.getPattern()) || oldConfig.getLimit() != newConfig.getLimit() || oldConfig.getCount() != newConfig.getCount()) {
					logger.log(Level.FINE, "Logging configuration has changed; closing and removing old {0}...", fileHandler.getClass().getSimpleName());
					LoggingSupport.getRootLogger().removeHandler(fileHandler);
					fileHandler.close();
					fileHandler = null;
					logger.log(Level.FINE, "Old FileHandler closed and removed.");
				}
			}

			if (fileHandler == null) {
				logger.log(Level.FINE, "FileHandler not found; creating one...");
				try {
					final File logDir = new File(fileHandlerPattern).getParentFile();
					if (logDir != null) {
						logDir.mkdirs();
					}
					fileHandler = new EnhancedFileHandler(newConfig);
					LoggingSupport.getRootLogger().addHandler(fileHandler);
					logger.log(Level.FINE, "{0} created successfully.", fileHandler.getClass().getSimpleName());
				}
				catch (final IOException ioe) {
					logger.log(Level.SEVERE, ioe.toString(), ioe);
				}
			}
		}
	}

	protected void disableLoggingFileHandler() {
		if (fileHandler != null) {
			LoggingSupport.getRootLogger().removeHandler(fileHandler);
			fileHandler.close();
			fileHandler = null;
			logger.log(Level.FINE, "FileHandler closed and removed.");
		}
	}

	protected void updateLoggingLevel() {
		try {
			LoggingSupport.setLevel(LoggingSupport.getRootLogger().getName(), Level.parse(getLoggingLevel()));
		}
		catch (final IllegalArgumentException iae) {
			logger.log(Level.WARNING, iae.toString(), iae);
		}
	}

	protected abstract String getFileHandlerPattern();

	protected boolean isFileHandlerEnabled() {
		return DEFAULT_LOGGING_FILES_ENABLED;
	}

	protected int getFileHandlerLimit() {
		return DEFAULT_LOGGING_FILES_LIMIT * 1024;
	}

	protected int getFileHandlerCount() {
		return DEFAULT_LOGGING_FILES_COUNT;
	}

	protected String getFileHandlerFormat() {
		return DEFAULT_LOG_FORMAT_FILE;
	}

	protected String getLoggingLevel() {
		return DEFAULT_LOGGING_LEVEL.getName();
	}

}
