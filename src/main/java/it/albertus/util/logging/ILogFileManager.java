package it.albertus.util.logging;

import java.io.File;

public interface ILogFileManager {

	File[] listFiles();

	boolean deleteFile(File file);

}
