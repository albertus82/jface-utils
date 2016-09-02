package it.albertus.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IConfiguration {

	void reload();

	File getFile();

	InputStream openInputStream() throws IOException;

	OutputStream openOutputStream() throws IOException;

}
