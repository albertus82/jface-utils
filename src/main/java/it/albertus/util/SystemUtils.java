package it.albertus.util;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileSystemView;

public class SystemUtils {

	private static final String USER_HOME = "user.home";
	private static final String OS_NAME = "os.name";

	private static String osSpecificDocumentsDir; // Cache
	private static String osSpecificConfigurationDir; // Cache
	private static String osSpecificLocalAppDataDir; // Cache

	private SystemUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static synchronized String getOsSpecificConfigurationDir() {
		if (osSpecificConfigurationDir == null) {
			final String os = StringUtils.trimToEmpty(System.getProperty(OS_NAME)).toLowerCase(Locale.ROOT);
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
			final String os = StringUtils.trimToEmpty(System.getProperty(OS_NAME)).toLowerCase(Locale.ROOT);
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
			final String os = StringUtils.trimToEmpty(System.getProperty(OS_NAME)).toLowerCase(Locale.ROOT);
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

}
