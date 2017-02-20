package it.albertus.util.logging;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLogFileManager implements ILogFileManager {

	protected static final String LOG_FILE_EXTENSION = ".log";
	protected static final String LOCK_FILE_EXTENSION = ".lck";

	protected final FilenameFilter logFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
			return name != null && name.toLowerCase().contains(getLogFileExtension()) && !name.endsWith(getLockFileExtension());
		}
	};

	protected final FilenameFilter lockFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
			return name != null && name.endsWith(getLockFileExtension());
		}
	};

	/**
	 * This method must return the directory that is configured as the log files
	 * path.
	 * 
	 * @return the directory that contains the logs.
	 */
	public abstract String getPath();

	@Override
	public File[] listFiles() {
		return new File(getPath()).listFiles(logFilenameFilter);
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
		final File[] lockFiles = new File(getPath()).listFiles(lockFilenameFilter);
		if (lockFiles != null) {
			for (final File lockFile : lockFiles) {
				final File lockedFile = new File(lockFile.getPath().replace(LOCK_FILE_EXTENSION, ""));
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

	public String getLogFileExtension() {
		return LOG_FILE_EXTENSION;
	}

	public String getLockFileExtension() {
		return LOCK_FILE_EXTENSION;
	}

}
