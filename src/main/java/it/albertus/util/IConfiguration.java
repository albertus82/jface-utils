package it.albertus.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface IConfiguration {

	String getString(String key);

	String getString(String key, String defaultValue);

	String getString(String key, boolean emptyIfNull);

	char[] getCharArray(String key);

	Boolean getBoolean(String key);

	boolean getBoolean(String key, boolean defaultValue);

	Long getLong(String key);

	long getLong(String key, long defaultValue);

	Integer getInt(String key);

	int getInt(String key, int defaultValue);

	Short getShort(String key);

	short getShort(String key, short defaultValue);

	Byte getByte(String key);

	byte getByte(String key, byte defaultValue);

	Float getFloat(String key);

	float getFloat(String key, float defaultValue);

	Double getDouble(String key);

	double getDouble(String key, double defaultValue);

	BigDecimal getBigDecimal(String key);

	BigDecimal getBigDecimal(String key, BigDecimal defaultValue);

	BigInteger getBigInteger(String key);

	BigInteger getBigInteger(String key, BigInteger defaultValue);

	Character getChar(String key);

	char getChar(String key, char defaultValue);

	boolean contains(String key);

}
