package it.albertus.jface.preference;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.preference.PreferenceStore;

import it.albertus.util.config.Configuration;
import it.albertus.util.logging.LoggerFactory;

public class ConfigurationStore extends PreferenceStore {

	private static final Logger log = LoggerFactory.getLogger(ConfigurationStore.class);

	protected final String filename;

	public ConfigurationStore(final String filename) {
		super(filename);
		this.filename = filename;
	}

	@Override
	public void save() throws IOException {
		final File parentFile = new File(filename).getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs(); // Create directories if not exists
		}
		super.save();
	}

	@Override
	public boolean getBoolean(final String name) {
		return Configuration.parseBoolean(getString(name).trim());
	}

	@Override
	public boolean getDefaultBoolean(final String name) {
		return Configuration.parseBoolean(getDefaultString(name).trim());
	}

	@Override
	public int getInt(final String name) {
		try {
			return Integer.parseInt(getString(name).trim());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable int:", e);
			return INT_DEFAULT_DEFAULT;
		}
	}

	@Override
	public int getDefaultInt(final String name) {
		try {
			return Integer.parseInt(getDefaultString(name).trim());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable int:", e);
			return INT_DEFAULT_DEFAULT;
		}
	}

	@Override
	public long getLong(final String name) {
		try {
			return Long.parseLong(getString(name).trim());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable long:", e);
			return LONG_DEFAULT_DEFAULT;
		}
	}

	@Override
	public long getDefaultLong(final String name) {
		try {
			return Long.parseLong(getDefaultString(name).trim());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable long:", e);
			return LONG_DEFAULT_DEFAULT;
		}
	}

	@Override
	public float getFloat(final String name) {
		try {
			return Float.parseFloat(getString(name).trim());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable float:", e);
			return FLOAT_DEFAULT_DEFAULT;
		}
	}

	@Override
	public float getDefaultFloat(final String name) {
		try {
			return Float.parseFloat(getDefaultString(name).trim());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable float:", e);
			return FLOAT_DEFAULT_DEFAULT;
		}
	}

	@Override
	public double getDouble(final String name) {
		try {
			return Double.parseDouble(getString(name).trim());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable double:", e);
			return DOUBLE_DEFAULT_DEFAULT;
		}
	}

	@Override
	public double getDefaultDouble(final String name) {
		try {
			return Double.parseDouble(getDefaultString(name).trim());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable double:", e);
			return DOUBLE_DEFAULT_DEFAULT;
		}
	}

}
