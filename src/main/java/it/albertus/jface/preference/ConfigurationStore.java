package it.albertus.jface.preference;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.preference.PreferenceStore;

import it.albertus.util.Configuration;

public class ConfigurationStore extends PreferenceStore {

	protected final String filename;

	public ConfigurationStore(String filename) {
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
		return Configuration.parseBoolean(getString(name));
	}

	@Override
	public boolean getDefaultBoolean(final String name) {
		return Configuration.parseBoolean(getDefaultString(name));
	}

	@Override
	public int getInt(final String name) {
		try {
			return Integer.parseInt(getString(name).trim());
		}
		catch (final NumberFormatException nfe) {
			return INT_DEFAULT_DEFAULT;
		}
	}

	@Override
	public int getDefaultInt(final String name) {
		try {
			return Integer.parseInt(getDefaultString(name).trim());
		}
		catch (final NumberFormatException nfe) {
			return INT_DEFAULT_DEFAULT;
		}
	}

	@Override
	public long getLong(final String name) {
		try {
			return Long.parseLong(getString(name).trim());
		}
		catch (final NumberFormatException nfe) {
			return LONG_DEFAULT_DEFAULT;
		}
	}

	@Override
	public long getDefaultLong(final String name) {
		try {
			return Long.parseLong(getDefaultString(name).trim());
		}
		catch (final NumberFormatException nfe) {
			return LONG_DEFAULT_DEFAULT;
		}
	}

	@Override
	public float getFloat(final String name) {
		try {
			return Float.parseFloat(getString(name).trim());
		}
		catch (final NumberFormatException nfe) {
			return FLOAT_DEFAULT_DEFAULT;
		}
	}

	@Override
	public float getDefaultFloat(final String name) {
		try {
			return Float.parseFloat(getDefaultString(name).trim());
		}
		catch (final NumberFormatException nfe) {
			return FLOAT_DEFAULT_DEFAULT;
		}
	}

	@Override
	public double getDouble(final String name) {
		try {
			return Double.parseDouble(getString(name).trim());
		}
		catch (final NumberFormatException nfe) {
			return DOUBLE_DEFAULT_DEFAULT;
		}
	}

	@Override
	public double getDefaultDouble(final String name) {
		try {
			return Double.parseDouble(getDefaultString(name).trim());
		}
		catch (final NumberFormatException nfe) {
			return DOUBLE_DEFAULT_DEFAULT;
		}
	}

}
