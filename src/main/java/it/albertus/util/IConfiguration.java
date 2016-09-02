package it.albertus.util;

import java.io.File;
import java.io.IOException;

public interface IConfiguration {

	void reload() throws IOException;

	File getFile();

}
