package it.albertus.util.logging;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLogFileManager implements ILogFileManager {

	public static final String LOG_FILE_EXTENSION = ".log";
	public static final String LOCK_FILE_EXTENSION = ".lck";

	private final FilenameFilter logFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
			return name != null && name.toLowerCase().contains(getLogFileExtension()) && !name.endsWith(getLockFileExtension());
		}
	};

	private final FilenameFilter lockFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
			return name != null && name.endsWith(getLockFileExtension());
		}
	};

	/**
	 * This method must return the directory that is configured as the
	 * destination path for the application's log files.
	 * 
	 * @return the directory that contains the application's logs.
	 */
	public abstract String getPath();

	@Override
	public File[] listFiles() {
		return new File(getPath()).listFiles(getLogFilenameFilter());
	}

	@Override
	public boolean deleteFile(final File file) {
		if (getLockedFiles().contains(file)) {
			return false;
		}
		else {
			return file.delete();
		}
	}

	public Set<File> getLockedFiles() {
		final Set<File> lockedFiles = new HashSet<File>();
		final File[] lockFiles = new File(getPath()).listFiles(getLockFilenameFilter());
		if (lockFiles != null) {
			for (final File lockFile : lockFiles) {
				final File lockedFile = new File(lockFile.getPath().replace(getLockFileExtension(), ""));
				if (lockedFile.exists()) {
					lockedFiles.add(lockedFile);
				}
			}
		}
		return lockedFiles;
	}

	public int deleteAllFiles() {
		int count = 0;
		final File[] files = listFiles();
		if (files != null) {
			for (final File file : files) {
				if (!file.isDirectory()) {
					count += deleteFile(file) ? 1 : 0;
				}
			}
		}
		return count;
	}

	public FilenameFilter getLogFilenameFilter() {
		return logFilenameFilter;
	}

	public FilenameFilter getLockFilenameFilter() {
		return lockFilenameFilter;
	}

	public String getLogFileExtension() {
		return LOG_FILE_EXTENSION;
	}

	public String getLockFileExtension() {
		return LOCK_FILE_EXTENSION;
	}

}
