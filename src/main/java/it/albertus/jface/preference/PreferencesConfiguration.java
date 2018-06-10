package it.albertus.jface.preference;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;

import it.albertus.util.Configuration;
import it.albertus.util.IConfiguration;
import it.albertus.util.IPropertiesConfiguration;

public class PreferencesConfiguration implements IPreferencesConfiguration, IConfiguration, IPropertiesConfiguration, IPreferencesCallback {

	private final Configuration configuration;

	public PreferencesConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public String getString(IPreference preference) {
		return getString(preference.getName());
	}

	@Override
	public String getString(IPreference preference, String defaultValue) {
		return getString(preference.getName(), defaultValue);
	}

	@Override
	public String getString(IPreference preference, boolean emptyIfNull) {
		return getString(preference.getName(), emptyIfNull);
	}

	@Override
	public char[] getCharArray(IPreference preference) {
		return getCharArray(preference.getName());
	}

	@Override
	public Boolean getBoolean(IPreference preference) {
		return getBoolean(preference.getName());
	}

	@Override
	public boolean getBoolean(IPreference preference, boolean defaultValue) {
		return getBoolean(preference.getName(), defaultValue);
	}

	@Override
	public Long getLong(IPreference preference) {
		return getLong(preference.getName());
	}

	@Override
	public long getLong(IPreference preference, long defaultValue) {
		return getLong(preference.getName(), defaultValue);
	}

	@Override
	public Integer getInt(IPreference preference) {
		return getInt(preference.getName());
	}

	@Override
	public int getInt(IPreference preference, int defaultValue) {
		return getInt(preference.getName(), defaultValue);
	}

	@Override
	public Short getShort(IPreference preference) {
		return getShort(preference.getName());
	}

	@Override
	public short getShort(IPreference preference, short defaultValue) {
		return getShort(preference.getName(), defaultValue);
	}

	@Override
	public Byte getByte(IPreference preference) {
		return getByte(preference.getName());
	}

	@Override
	public byte getByte(IPreference preference, byte defaultValue) {
		return getByte(preference.getName(), defaultValue);
	}

	@Override
	public Float getFloat(IPreference preference) {
		return getFloat(preference.getName());
	}

	@Override
	public float getFloat(IPreference preference, float defaultValue) {
		return getFloat(preference.getName(), defaultValue);
	}

	@Override
	public Double getDouble(IPreference preference) {
		return getDouble(preference.getName());
	}

	@Override
	public double getDouble(IPreference preference, double defaultValue) {
		return getDouble(preference.getName(), defaultValue);
	}

	@Override
	public BigDecimal getBigDecimal(IPreference preference) {
		return getBigDecimal(preference.getName());
	}

	@Override
	public BigDecimal getBigDecimal(IPreference preference, BigDecimal defaultValue) {
		return getBigDecimal(preference.getName(), defaultValue);
	}

	@Override
	public BigInteger getBigInteger(IPreference preference) {
		return getBigInteger(preference.getName());
	}

	@Override
	public BigInteger getBigInteger(IPreference preference, BigInteger defaultValue) {
		return getBigInteger(preference.getName(), defaultValue);
	}

	@Override
	public Character getChar(IPreference preference) {
		return getChar(preference.getName());
	}

	@Override
	public char getChar(IPreference preference, char defaultValue) {
		return getChar(preference.getName(), defaultValue);
	}

	@Override
	public boolean contains(IPreference preference) {
		return contains(preference.getName());
	}

	// Pass-through methods follows...
	@Override
	public String getString(String key) {
		return configuration.getString(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return configuration.getString(key, defaultValue);
	}

	@Override
	public String getString(String key, boolean emptyIfNull) {
		return configuration.getString(key, emptyIfNull);
	}

	@Override
	public char[] getCharArray(String key) {
		return configuration.getCharArray(key);
	}

	@Override
	public Boolean getBoolean(String key) {
		return configuration.getBoolean(key);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	@Override
	public Long getLong(String key) {
		return configuration.getLong(key);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	@Override
	public Integer getInt(String key) {
		return configuration.getInt(key);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return configuration.getInt(key, defaultValue);
	}

	@Override
	public Short getShort(String key) {
		return configuration.getShort(key);
	}

	@Override
	public short getShort(String key, short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	@Override
	public Byte getByte(String key) {
		return configuration.getByte(key);
	}

	@Override
	public byte getByte(String key, byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	@Override
	public Float getFloat(String key) {
		return configuration.getFloat(key);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	@Override
	public Double getDouble(String key) {
		return configuration.getDouble(key);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		return configuration.getBigDecimal(key);
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return configuration.getBigDecimal(key, defaultValue);
	}

	@Override
	public BigInteger getBigInteger(String key) {
		return configuration.getBigInteger(key);
	}

	@Override
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return configuration.getBigInteger(key, defaultValue);
	}

	@Override
	public Character getChar(String key) {
		return configuration.getChar(key);
	}

	@Override
	public char getChar(String key, char defaultValue) {
		return configuration.getChar(key, defaultValue);
	}

	@Override
	public boolean contains(String key) {
		return configuration.contains(key);
	}

	@Override
	public void reload() throws IOException {
		configuration.reload();
	}

	@Override
	public String getFileName() {
		return configuration.getFileName();
	}

	@Override
	public Properties getProperties() {
		return configuration.getProperties();
	}

	@Override
	public void save() throws IOException {
		configuration.save();
	}

	@Override
	public String toString() {
		return configuration.toString();
	}

}
