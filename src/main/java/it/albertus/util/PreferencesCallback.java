package it.albertus.util;

import java.io.File;

public abstract class PreferencesCallback implements IConfiguration {

	private final String fileName;

	public PreferencesCallback(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	public File getFile() {
		return new File(fileName);
	}

	public String getFileName() {
		return fileName;
	}

}
