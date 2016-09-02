package it.albertus.jface.preference;

import java.io.IOException;

public interface IPreferencesCallback {

	void reload() throws IOException;

	String getFileName();

}
