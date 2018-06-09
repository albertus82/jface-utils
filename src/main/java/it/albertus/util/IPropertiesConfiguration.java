package it.albertus.util;

import java.io.IOException;
import java.util.Properties;

public interface IPropertiesConfiguration {

	Properties getProperties();

	void save() throws IOException;

}
