package it.albertus.util;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * Conviene estendere questa classe con un <b>Singleton</b> che richiami il
 * costruttore {@link #Configuration(String)} passando come parametro il nome
 * del file di configurazione da caricare.
 */
public class Configuration extends PropertiesConfiguration {

	public Configuration(final String fileName) {
		super(fileName);
	}

	@Override
	public File getFile() {
		File config;
		final String fileName = getFileName();
		try {
			final String parent = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getSchemeSpecificPart()).getParent();
			config = new File((parent != null ? parent : "") + File.separator + fileName);
		}
		catch (final Exception e) {
			config = new File(fileName);
		}
		return config;
	}

	public String getString(final String key) {
		return getProperties().getProperty(key);
	}

	public String getString(final String key, final String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}

	public char[] getCharArray(final String key) {
		try {
			return getProperties().getProperty(key).toCharArray();
		}
		catch (final Exception e) {
			return null;
		}
	}

	public Boolean getBoolean(final String key) {
		final String value = getString(key);
		if (value != null) {
			return Boolean.valueOf(value.trim());
		}
		return null;
	}

	public boolean getBoolean(final String key, final boolean defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Boolean.parseBoolean(value.trim());
		}
		return defaultValue;
	}

	public Long getLong(final String key) {
		final String value = getString(key);
		if (value != null) {
			return Long.valueOf(value.trim());
		}
		return null;
	}

	public long getLong(final String key, final long defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Long.parseLong(value.trim());
		}
		return defaultValue;
	}

	public Integer getInt(final String key) {
		final String value = getString(key);
		if (value != null) {
			return Integer.valueOf(value.trim());
		}
		return null;
	}

	public int getInt(final String key, final int defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Integer.parseInt(value.trim());
		}
		return defaultValue;
	}

	public Short getShort(final String key) {
		final String value = getString(key);
		if (value != null) {
			return Short.valueOf(value.trim());
		}
		return null;
	}

	public short getShort(final String key, final short defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Short.parseShort(value.trim());
		}
		return defaultValue;
	}

	public Byte getByte(final String key) {
		final String value = getString(key);
		if (value != null) {
			return Byte.valueOf(value.trim());
		}
		return null;
	}

	public byte getByte(final String key, final byte defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Byte.parseByte(value.trim());
		}
		return defaultValue;
	}

	public Float getFloat(final String key) {
		final String value = getString(key);
		if (value != null) {
			return Float.valueOf(value.trim());
		}
		return null;
	}

	public float getFloat(final String key, final float defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Float.parseFloat(value.trim());
		}
		return defaultValue;
	}

	public Double getDouble(final String key) {
		final String value = getString(key);
		if (value != null) {
			return Double.valueOf(value.trim());
		}
		return null;
	}

	public double getDouble(final String key, final double defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return Double.parseDouble(value.trim());
		}
		return defaultValue;
	}

	public Character getChar(final String key) {
		final String value = getString(key);
		if (value != null) {
			return parseChar(value);
		}
		return null;
	}

	public char getChar(final String key, final char defaultValue) {
		final String value = getString(key);
		if (value != null) {
			return parseChar(value);
		}
		return defaultValue;
	}

	private char parseChar(final String value) {
		if (value.length() == 1) {
			return value.charAt(0);
		}
		else {
			throw new IllegalArgumentException("value length != 1");
		}
	}

	public boolean contains(final String key) {
		return getProperties().get(key) != null;
	}

	@Override
	public String toString() {
		final Map<String, String> properties = new TreeMap<String, String>();
		for (final Object key : getProperties().keySet()) {
			properties.put((String) key, getProperties().getProperty((String) key));
		}
		return properties.toString();
	}

}
