package it.albertus.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import it.albertus.jface.preference.PreferencesCallback;

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
			inputStream = new FileInputStream(getFileName());
			if (inputStream != null) {
				synchronized (properties) {
					try {
						properties.clear();
						properties.load(inputStream); // buffered internally
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
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch (final IOException ioe) {/* Ignore */}
		}
	}

}
