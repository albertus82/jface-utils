package it.albertus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import it.albertus.jface.preference.PreferencesCallback;

public class PropertiesConfiguration extends PreferencesCallback {

	private final Properties properties;

	public PropertiesConfiguration(final String propertiesFileName) throws IOException {
		super(propertiesFileName);
		this.properties = new Properties();
		load();
	}

	@Override
	public void reload() throws IOException {
		load();
	}

	public Properties getProperties() {
		return properties;
	}

	protected void load() throws IOException {
		final File file = new File(getFileName());
		if (file.exists()) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				synchronized (properties) {
					properties.clear();
					properties.load(inputStream); // buffered internally
				}
			}
			finally {
				IOUtils.closeQuietly(inputStream);
			}
		}
	}

	public void save() throws IOException {
		final File file = new File(getFileName());
		file.getParentFile().mkdirs();
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			getProperties().store(os, null);
		}
		finally {
			IOUtils.closeQuietly(os);
		}
	}

}
