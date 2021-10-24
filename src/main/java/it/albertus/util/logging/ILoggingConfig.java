package it.albertus.util.logging;

public interface ILoggingConfig {

	String getFileHandlerPattern();

	boolean isFileHandlerEnabled();

	int getFileHandlerLimit();

	int getFileHandlerCount();

	String getFileHandlerFormat();

	String getLoggingLevel();

}
