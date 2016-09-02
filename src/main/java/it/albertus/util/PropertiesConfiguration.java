package it.albertus.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfiguration implements IConfiguration {

	private final Properties properties;
	private final String fileName;

	public PropertiesConfiguration(final String propertiesFileName) {
		this.properties = new Properties();
		this.fileName = propertiesFileName;
		load();
	}

	@Override
	public void reload() {
		load();
	}

	@Override
	public File getFile() {
		return new File(fileName);
	}

	public Properties getProperties() {
		return properties;
	}

	public final String getFileName() {
		return fileName;
	}

	protected void load() {
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(getFile()));
			if (inputStream != null) {
				synchronized (properties) {
					try {
						properties.clear();
						properties.load(inputStream);
					}
					catch (final IOException ioe) {
						throw new RuntimeException(ioe);
					}
				}
			}
		}
		catch (final FileNotFoundException fnfe) {/* Ignore */}
		finally {
			try {
				inputStream.close();
			}
			catch (final Exception e) {/* Ignore */}
		}
	}

}
