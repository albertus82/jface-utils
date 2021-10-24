package io.github.albertus82.util.logging;

public interface ILoggingConfig {

	String getFileHandlerPattern();

	boolean isFileHandlerEnabled();

	int getFileHandlerLimit();

	int getFileHandlerCount();

	String getFileHandlerFormat();

	String getLoggingLevel();

}
