package it.albertus.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfiguration extends PreferencesCallback {

	private final Properties properties;

	public PropertiesConfiguration(final String propertiesFileName) {
		super(propertiesFileName);
		this.properties = new Properties();
		load();
	}

	@Override
	public void reload() {
		load();
	}

	public Properties getProperties() {
		return properties;
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
