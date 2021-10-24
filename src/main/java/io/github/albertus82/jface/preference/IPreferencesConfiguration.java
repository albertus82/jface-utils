package io.github.albertus82.jface.preference;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.github.albertus82.util.config.IConfiguration;

public interface IPreferencesConfiguration extends IConfiguration {

	String getString(IPreference preference);

	String getString(IPreference preference, String defaultValue);

	String getString(IPreference preference, boolean emptyIfNull);

	char[] getCharArray(IPreference preference);

	Boolean getBoolean(IPreference preference);

	boolean getBoolean(IPreference preference, boolean defaultValue);

	Long getLong(IPreference preference);

	long getLong(IPreference preference, long defaultValue);

	Integer getInt(IPreference preference);

	int getInt(IPreference preference, int defaultValue);

	Short getShort(IPreference preference);

	short getShort(IPreference preference, short defaultValue);

	Byte getByte(IPreference preference);

	byte getByte(IPreference preference, byte defaultValue);

	Float getFloat(IPreference preference);

	float getFloat(IPreference preference, float defaultValue);

	Double getDouble(IPreference preference);

	double getDouble(IPreference preference, double defaultValue);

	BigDecimal getBigDecimal(IPreference preference);

	BigDecimal getBigDecimal(IPreference preference, BigDecimal defaultValue);

	BigInteger getBigInteger(IPreference preference);

	BigInteger getBigInteger(IPreference preference, BigInteger defaultValue);

	Character getChar(IPreference preference);

	char getChar(IPreference preference, char defaultValue);

	boolean contains(IPreference preference);

}
