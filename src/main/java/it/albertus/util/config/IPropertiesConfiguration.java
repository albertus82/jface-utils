package it.albertus.util.config;

import java.io.IOException;
import java.util.Properties;

import it.albertus.jface.preference.IPreferencesCallback;

public interface IPropertiesConfiguration extends IPreferencesCallback {

	Properties getProperties();

	void save() throws IOException;

}
