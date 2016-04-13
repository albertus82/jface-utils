package it.albertus.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Classe astratta da estendere con una classe <b>Singleton</b> che richiami il
 * costruttore {@link #Configuration(String)} passando come parametro il nome
 * del file di configurazione da caricare.
 */
public abstract class Configuration {

	private final Properties properties = new Properties();
	private final String fileName;

	protected Configuration(String fileName) {
		this.fileName = fileName;
		try {
			load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void load() throws IOException {
		final InputStream inputStream = openConfigurationInputStream();
		if (inputStream != null) {
			synchronized (properties) {
				properties.clear();
				properties.load(inputStream);
			}
			inputStream.close();
		}
	}

	public void reload() {
		try {
			load();
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	protected File getFile() {
		File config = null;
		try {
			config = new File(new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getSchemeSpecificPart()).getParent() + File.separator + fileName);
		}
		catch (final Exception e) {
			config = new File(fileName);
		}
		return config;
	}

	public InputStream openConfigurationInputStream() throws IOException {
		final InputStream inputStream;
		final File config = getFile();
		if (config != null && config.exists()) {
			inputStream = new BufferedInputStream(new FileInputStream(config));
		}
		else {
			inputStream = getClass().getResourceAsStream('/' + fileName);
		}
		return inputStream;
	}

	public OutputStream openConfigurationOutputStream() throws IOException {
		final OutputStream outputStream;
		final File config = getFile();
		outputStream = new BufferedOutputStream(new FileOutputStream(config));
		return outputStream;
	}

	public Properties getProperties() {
		return properties;
	}

	public String getFileName() {
		return fileName;
	}

	public String getString(String key) {
		return properties.getProperty(key);
	}

	public String getString(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public Boolean getBoolean(String key) {
		final String value = getString(key);
		if (value != null) {
			return Boolean.valueOf(value.trim());
		}
		return null;
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Boolean.parseBoolean(value.trim());
		}
		return defaultValue;
	}

	public Long getLong(String key) {
		final String value = getString(key);
		if (value != null) {
			return Long.valueOf(value);
		}
		return null;
	}

	public long getLong(String key, long defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Long.parseLong(value);
		}
		return defaultValue;
	}

	public Integer getInt(String key) {
		final String value = getString(key);
		if (value != null) {
			return Integer.valueOf(value);
		}
		return null;
	}

	public int getInt(String key, int defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Integer.parseInt(value);
		}
		return defaultValue;
	}

	public Short getShort(String key) {
		final String value = getString(key);
		if (value != null) {
			return Short.valueOf(value);
		}
		return null;
	}

	public short getShort(String key, short defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Short.parseShort(value);
		}
		return defaultValue;
	}

	public Byte getByte(String key) {
		final String value = getString(key);
		if (value != null) {
			return Byte.valueOf(value);
		}
		return null;
	}

	public byte getByte(String key, byte defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Byte.parseByte(value);
		}
		return defaultValue;
	}

	public Float getFloat(String key) {
		final String value = getString(key);
		if (value != null) {
			return Float.valueOf(value);
		}
		return null;
	}

	public float getFloat(String key, float defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Float.parseFloat(value);
		}
		return defaultValue;
	}

	public Double getDouble(String key) {
		final String value = getString(key);
		if (value != null) {
			return Double.valueOf(value);
		}
		return null;
	}

	public double getDouble(String key, double defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Double.parseDouble(value);
		}
		return defaultValue;
	}

	public Character getChar(String key) {
		final String value = getString(key);
		if (value != null) {
			return parseChar(value);
		}
		return null;
	}

	public char getChar(String key, char defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return parseChar(value);
		}
		return defaultValue;
	}

	private char parseChar(String value) {
		if (value.length() == 1) {
			return value.charAt(0);
		}
		else {
			throw new IllegalArgumentException("value length != 1");
		}
	}

	public boolean contains(String key) {
		return properties.get(key) != null;
	}

	@Override
	public String toString() {
		final Map<String, String> properties = new TreeMap<String, String>();
		for (final Object key : this.properties.keySet()) {
			properties.put((String) key, this.properties.getProperty((String) key));
		}
		return properties.toString();
	}

}
