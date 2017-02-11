package it.albertus.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import it.albertus.jface.JFaceMessages;
import it.albertus.util.logging.LoggerFactory;

public class Configuration extends PropertiesConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

	private static final String USER_HOME = "user.home";
	private static final String OS_NAME = "os.name";

	private static String osSpecificDocumentsDir; // Cache
	private static String osSpecificConfigurationDir; // Cache
	private static String osSpecificLocalAppDataDir; // Cache

	public Configuration(final String fileName) throws IOException {
		super(fileName);
	}

	public Configuration(final String fileName, final boolean prependOsSpecificConfigurationDir) throws IOException {
		super(prependOsSpecificConfigurationDir ? getOsSpecificConfigurationDir() + File.separator + fileName : fileName);
	}

	public static synchronized String getOsSpecificConfigurationDir() {
		if (osSpecificConfigurationDir == null) {
			final String os = StringUtils.trimToEmpty(System.getProperty(OS_NAME)).toLowerCase();
			if (os.contains("win") && System.getenv("APPDATA") != null) {
				osSpecificConfigurationDir = System.getenv("APPDATA");
			}
			else if (os.contains("mac")) {
				osSpecificConfigurationDir = System.getProperty(USER_HOME) + File.separator + "Library" + File.separator + "Preferences";
			}
			else {
				osSpecificConfigurationDir = System.getProperty(USER_HOME);
			}
		}
		return osSpecificConfigurationDir;
	}

	public static synchronized String getOsSpecificLocalAppDataDir() {
		if (osSpecificLocalAppDataDir == null) {
			final String os = StringUtils.trimToEmpty(System.getProperty(OS_NAME)).toLowerCase();
			if (os.contains("win") && System.getenv("LOCALAPPDATA") != null) {
				osSpecificLocalAppDataDir = System.getenv("LOCALAPPDATA");
			}
			else if (os.contains("mac")) {
				osSpecificLocalAppDataDir = System.getProperty(USER_HOME) + File.separator + "Library";
			}
			else {
				osSpecificLocalAppDataDir = System.getProperty(USER_HOME);
			}
		}
		return osSpecificLocalAppDataDir;
	}

	public static synchronized String getOsSpecificDocumentsDir() {
		if (osSpecificDocumentsDir == null) {
			final String os = StringUtils.trimToEmpty(System.getProperty(OS_NAME)).toLowerCase();
			if (os.contains("win")) {
				osSpecificDocumentsDir = FileSystemView.getFileSystemView().getDefaultDirectory().getPath(); // slow and not thread-safe!
			}
			else if (os.contains("mac")) {
				osSpecificDocumentsDir = System.getProperty(USER_HOME) + File.separator + "Documents";
			}
			else {
				osSpecificDocumentsDir = System.getProperty(USER_HOME);
			}
		}
		return osSpecificDocumentsDir;
	}

	public String getString(final String key) {
		return getProperties().getProperty(key);
	}

	public String getString(final String key, final String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}

	public String getString(final String key, final boolean emptyIfNull) {
		return emptyIfNull ? getString(key, "") : getString(key);
	}

	public char[] getCharArray(final String key) {
		final String value = getProperties().getProperty(key);
		if (value != null) {
			return value.toCharArray();
		}
		else {
			return null;
		}
	}

	public Boolean getBoolean(final String key) {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		return parseBoolean(trimmedValue);
	}

	public boolean getBoolean(final String key, final boolean defaultValue) {
		final Boolean value = getBoolean(key);
		return value != null ? value.booleanValue() : defaultValue;
	}

	public Long getLong(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		try {
			return Long.valueOf(trimmedValue);
		}
		catch (final NumberFormatException nfe) {
			throw new ConfigurationException(getInvalidNumberErrorMessage(key, Long.MIN_VALUE, Long.MAX_VALUE), nfe, key);
		}
	}

	public long getLong(final String key, final long defaultValue) {
		Long value;
		try {
			value = getLong(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidNumberErrorMessage(key, Long.MIN_VALUE, Long.MAX_VALUE, defaultValue));
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public Integer getInt(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		try {
			return Integer.valueOf(trimmedValue);
		}
		catch (final NumberFormatException nfe) {
			throw new ConfigurationException(getInvalidNumberErrorMessage(key, Integer.MIN_VALUE, Integer.MAX_VALUE), nfe, key);
		}
	}

	public int getInt(final String key, final int defaultValue) {
		Integer value;
		try {
			value = getInt(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidNumberErrorMessage(key, Integer.MIN_VALUE, Integer.MAX_VALUE, defaultValue));
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public Short getShort(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		try {
			return Short.valueOf(trimmedValue);
		}
		catch (final NumberFormatException nfe) {
			throw new ConfigurationException(getInvalidNumberErrorMessage(key, Short.MIN_VALUE, Short.MAX_VALUE), nfe, key);
		}
	}

	public short getShort(final String key, final short defaultValue) {
		Short value;
		try {
			value = getShort(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidNumberErrorMessage(key, Short.MIN_VALUE, Short.MAX_VALUE, defaultValue), e);
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public Byte getByte(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		try {
			return Byte.valueOf(trimmedValue);
		}
		catch (final NumberFormatException nfe) {
			throw new ConfigurationException(getInvalidNumberErrorMessage(key, Byte.MIN_VALUE, Byte.MAX_VALUE), nfe, key);
		}
	}

	public byte getByte(final String key, final byte defaultValue) {
		Byte value;
		try {
			value = getByte(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidNumberErrorMessage(key, Byte.MIN_VALUE, Byte.MAX_VALUE, defaultValue));
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public Float getFloat(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		try {
			return Float.valueOf(trimmedValue);
		}
		catch (final NumberFormatException nfe) {
			throw new ConfigurationException(getInvalidNumberErrorMessage(key, Float.MIN_VALUE, Float.MAX_VALUE), nfe, key);
		}
	}

	public float getFloat(final String key, final float defaultValue) {
		Float value;
		try {
			value = getFloat(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidNumberErrorMessage(key, Float.MIN_VALUE, Float.MAX_VALUE, defaultValue));
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public Double getDouble(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		try {
			return Double.valueOf(trimmedValue);
		}
		catch (final NumberFormatException nfe) {
			throw new ConfigurationException(getInvalidNumberErrorMessage(key, Double.MIN_VALUE, Double.MAX_VALUE), nfe, key);
		}
	}

	public double getDouble(final String key, final double defaultValue) {
		Double value;
		try {
			value = getDouble(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidNumberErrorMessage(key, Double.MIN_VALUE, Double.MAX_VALUE, defaultValue));
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public BigDecimal getBigDecimal(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		try {
			return new BigDecimal(trimmedValue);
		}
		catch (final NumberFormatException nfe) {
			throw new ConfigurationException(getInvalidNumberErrorMessage(key), nfe, key);
		}
	}

	public BigDecimal getBigDecimal(final String key, final BigDecimal defaultValue) {
		BigDecimal value;
		try {
			value = getBigDecimal(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidNumberErrorMessage(key, defaultValue));
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public BigInteger getBigInteger(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null) {
			return null;
		}
		final String trimmedValue = value.trim();
		if (trimmedValue.isEmpty()) {
			return null;
		}
		try {
			return new BigInteger(trimmedValue);
		}
		catch (final NumberFormatException nfe) {
			throw new ConfigurationException(getInvalidNumberErrorMessage(key), nfe, key);
		}
	}

	public BigInteger getBigInteger(final String key, final BigInteger defaultValue) {
		BigInteger value;
		try {
			value = getBigInteger(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidNumberErrorMessage(key, defaultValue));
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public Character getChar(final String key) throws ConfigurationException {
		final String value = getString(key);
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			return parseChar(value);
		}
		catch (final IllegalArgumentException iae) {
			throw new ConfigurationException(getInvalidCharacterErrorMessage(key), iae, key);
		}
	}

	public char getChar(final String key, final char defaultValue) {
		Character value;
		try {
			value = getChar(key);
		}
		catch (final ConfigurationException e) {
			logger.log(Level.FINE, e.toString(), e);
			logger.log(Level.WARNING, getInvalidCharacterErrorMessage(key, defaultValue));
			value = defaultValue;
		}
		return value != null ? value : defaultValue;
	}

	public boolean contains(final String key) {
		return getProperties().containsKey(key);
	}

	@Override
	public String toString() {
		final Map<String, String> properties = new TreeMap<String, String>();
		for (final Object key : getProperties().keySet()) {
			properties.put(key.toString(), key.toString().toLowerCase().contains("password") ? "**********" : getProperties().getProperty(key.toString()));
		}
		return properties.toString();
	}

	private String getInvalidCharacterErrorMessage(final String key) {
		return getInvalidCharacterErrorMessage(key, null);
	}

	private String getInvalidCharacterErrorMessage(final String key, final Character defaultValue) {
		final StringBuilder message = new StringBuilder(JFaceMessages.get("err.configuration.invalid.char", key));
		if (defaultValue != null) {
			message.append(' ').append(JFaceMessages.get("err.configuration.using.default", "'" + defaultValue.toString() + "'"));
		}
		return message.append(' ').append(JFaceMessages.get("err.configuration.review", getFileName())).toString();
	}

	private String getInvalidNumberErrorMessage(final String key) {
		return getInvalidNumberErrorMessage(key, null);
	}

	private String getInvalidNumberErrorMessage(final String key, final Number defaultValue) {
		return getInvalidNumberErrorMessage(key, null, null, defaultValue);
	}

	private String getInvalidNumberErrorMessage(final String key, final Number min, final Number max) {
		return getInvalidNumberErrorMessage(key, min, max, null);
	}

	private String getInvalidNumberErrorMessage(final String key, final Number min, final Number max, final Number defaultValue) {
		final StringBuilder message = new StringBuilder();
		if (min != null && max != null) {
			message.append(JFaceMessages.get("err.configuration.invalid.number", key, min, max));
		}
		else {
			message.append(JFaceMessages.get("err.configuration.invalid", key));
		}
		if (defaultValue != null) {
			message.append(' ').append(JFaceMessages.get("err.configuration.using.default", defaultValue));
		}
		return message.append(' ').append(JFaceMessages.get("err.configuration.review", getFileName())).toString();
	}

	public static boolean parseBoolean(final String value) {
		if ("1".equals(value)) {
			return true;
		}
		if ("0".equals(value)) {
			return false;
		}
		return Boolean.parseBoolean(value);
	}

	public static char parseChar(final String value) throws IllegalArgumentException {
		if (value.length() == 1) {
			return value.charAt(0);
		}
		else {
			throw new IllegalArgumentException("value length != 1");
		}
	}

}
