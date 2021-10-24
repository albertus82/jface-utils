package it.albertus.util.logging;

import java.util.logging.Level;

public abstract class LoggingDefaultConfig implements ILoggingConfig {

	public static final String DEFAULT_LOG_FORMAT_FILE = LoggingSupport.DEFAULT_FORMAT;
	public static final Level DEFAULT_LOGGING_LEVEL = Level.INFO;
	public static final boolean DEFAULT_LOGGING_FILES_ENABLED = true;
	public static final int DEFAULT_LOGGING_FILES_LIMIT_KB = 1024;
	public static final int DEFAULT_LOGGING_FILES_COUNT = 5;

	@Override
	public boolean isFileHandlerEnabled() {
		return DEFAULT_LOGGING_FILES_ENABLED;
	}

	@Override
	public int getFileHandlerLimit() {
		return DEFAULT_LOGGING_FILES_LIMIT_KB * 1024;
	}

	@Override
	public int getFileHandlerCount() {
		return DEFAULT_LOGGING_FILES_COUNT;
	}

	@Override
	public String getFileHandlerFormat() {
		return DEFAULT_LOG_FORMAT_FILE;
	}

	@Override
	public String getLoggingLevel() {
		return DEFAULT_LOGGING_LEVEL.getName();
	}

}
