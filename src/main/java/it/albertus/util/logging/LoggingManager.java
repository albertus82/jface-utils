package it.albertus.util.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingManager implements ILoggingManager {

	private static final Logger log = LoggerFactory.getLogger(LoggingManager.class);

	private final ILoggingConfig loggingConfig;

	private EnhancedFileHandler fileHandler;

	/**
	 * Create a new LoggingManager.
	 * 
	 * @param loggingConfig the logging configuration to use
	 * @param initialize when {@code true}, the manager will be initialized on
	 *        creation, otherwise a call to {@link #initializeLogging()} is needed
	 */
	public LoggingManager(final ILoggingConfig loggingConfig, final boolean initialize) {
		this.loggingConfig = loggingConfig;
		if (initialize) {
			initializeLogging();
		}
	}

	@Override
	public void initializeLogging() {
		if (LoggingSupport.getInitialConfigurationProperty() == null) {
			updateLoggingLevel();
			if (loggingConfig.isFileHandlerEnabled()) {
				enableLoggingFileHandler();
			}
			else {
				disableLoggingFileHandler();
			}
		}
	}

	protected void enableLoggingFileHandler() {
		final String fileHandlerPattern = loggingConfig.getFileHandlerPattern();
		if (fileHandlerPattern != null && !fileHandlerPattern.isEmpty()) {
			final FileHandlerConfig newConfig = new FileHandlerConfig();
			newConfig.setPattern(fileHandlerPattern);
			newConfig.setLimit(loggingConfig.getFileHandlerLimit());
			newConfig.setCount(loggingConfig.getFileHandlerCount());
			newConfig.setAppend(true);
			newConfig.setFormatter(new CustomFormatter(loggingConfig.getFileHandlerFormat()));

			if (fileHandler != null) {
				final FileHandlerConfig oldConfig = FileHandlerConfig.fromHandler(fileHandler);
				if (!oldConfig.getPattern().equals(newConfig.getPattern()) || oldConfig.getLimit() != newConfig.getLimit() || oldConfig.getCount() != newConfig.getCount()) {
					log.log(Level.FINE, "Logging configuration has changed; closing and removing old {0}...", fileHandler.getClass().getSimpleName());
					LoggingSupport.getRootLogger().removeHandler(fileHandler);
					fileHandler.close();
					fileHandler = null;
					log.log(Level.FINE, "Old FileHandler closed and removed.");
				}
			}

			if (fileHandler == null) {
				log.log(Level.FINE, "FileHandler not found; creating one...");
				try {
					final File logDir = new File(fileHandlerPattern).getParentFile();
					if (logDir != null) {
						logDir.mkdirs();
					}
					fileHandler = new EnhancedFileHandler(newConfig);
					LoggingSupport.getRootLogger().addHandler(fileHandler);
					log.log(Level.FINE, "{0} created successfully.", fileHandler.getClass().getSimpleName());
				}
				catch (final IOException e) {
					log.log(Level.SEVERE, "An error occurred while creating the FileHandler:", e);
				}
			}
		}
	}

	protected void disableLoggingFileHandler() {
		if (fileHandler != null) {
			LoggingSupport.getRootLogger().removeHandler(fileHandler);
			fileHandler.close();
			fileHandler = null;
			log.log(Level.FINE, "FileHandler closed and removed.");
		}
	}

	protected void updateLoggingLevel() {
		try {
			LoggingSupport.setLevel(LoggingSupport.getRootLogger().getName(), Level.parse(loggingConfig.getLoggingLevel()));
		}
		catch (final IllegalArgumentException e) {
			log.log(Level.WARNING, "Cannot update logging level:", e);
		}
	}

}
