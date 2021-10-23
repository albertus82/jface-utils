package io.github.albertus82.util.logging;

import java.io.File;

public interface ILogFileManager {

	File[] listFiles();

	boolean deleteFile(File file);

}
