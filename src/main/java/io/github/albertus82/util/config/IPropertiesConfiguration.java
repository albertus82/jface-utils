package io.github.albertus82.util.config;

import java.io.IOException;
import java.util.Properties;

import io.github.albertus82.jface.preference.IPreferencesCallback;

public interface IPropertiesConfiguration extends IPreferencesCallback {

	Properties getProperties();

	void save() throws IOException;

}
