package io.github.albertus82.jface.preference;

import java.io.IOException;

public interface IPreferencesCallback {

	void reload() throws IOException;

	String getFileName();

}
