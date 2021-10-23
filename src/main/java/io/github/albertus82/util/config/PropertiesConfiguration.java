package io.github.albertus82.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import io.github.albertus82.util.IOUtils;
import io.github.albertus82.util.SystemUtils;

public class PropertiesConfiguration implements IPropertiesConfiguration {

	static final String PASSWORD_PLACEHOLDER = "********"; // NOSONAR

	private final String fileName;
	private final Properties properties = new Properties();

	public PropertiesConfiguration(final String propertiesFileName) throws IOException {
		this.fileName = propertiesFileName;
		load();
	}

	public PropertiesConfiguration(final String propertiesFileName, final boolean prependOsSpecificConfigurationDir) throws IOException {
		this(prependOsSpecificConfigurationDir ? SystemUtils.getOsSpecificConfigurationDir() + File.separator + propertiesFileName : propertiesFileName);
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void reload() throws IOException {
		load();
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public void save() throws IOException {
		final File file = new File(getFileName());
		file.getParentFile().mkdirs();
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			properties.store(outputStream, null);
		}
		finally {
			IOUtils.closeQuietly(outputStream);
		}
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

	@Override
	public String toString() {
		final Map<String, String> props = new TreeMap<String, String>();
		for (final Object key : getProperties().keySet()) {
			props.put(key.toString(), (key.toString().toLowerCase().contains("password") || key.toString().toLowerCase(Locale.ROOT).contains("password")) ? PASSWORD_PLACEHOLDER : getProperties().getProperty(key.toString()));
		}
		return props.toString();
	}

}
