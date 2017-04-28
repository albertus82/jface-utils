package it.albertus.util.logging;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

import it.albertus.util.Supplier;

public class LogFileManager implements ILogFileManager {

	public static final class Defaults {
		public static final String LOG_FILE_EXTENSION = ".log";
		public static final String LOCK_FILE_EXTENSION = ".lck";

		private Defaults() {
			throw new IllegalAccessError("Constants class");
		}
	}

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

	private Supplier<String> path;
	private String logFileExtension;
	private String lockFileExtension;

	public LogFileManager(final Supplier<String> path) {
		this(path, Defaults.LOG_FILE_EXTENSION, Defaults.LOCK_FILE_EXTENSION);
	}

	public LogFileManager(final String path) {
		this(path, Defaults.LOG_FILE_EXTENSION, Defaults.LOCK_FILE_EXTENSION);
	}

	public LogFileManager(final String path, final String logFileExtension, final String lockFileExtension) {
		this(new Supplier<String>() {
			@Override
			public String get() {
				return path;
			}
		}, logFileExtension, lockFileExtension);
	}

	public LogFileManager(final Supplier<String> path, final String logFileExtension, final String lockFileExtension) {
		this.path = path;
		this.logFileExtension = logFileExtension;
		this.lockFileExtension = lockFileExtension;
	}

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

	/**
	 * Returns the directory that is configured as the destination path for the
	 * application's log files.
	 * 
	 * @return the directory that contains the application's logs.
	 */
	public String getPath() {
		return path.get();
	}

	public void setPath(final Supplier<String> path) {
		this.path = path;
	}

	public void setPath(final String path) {
		setPath(new Supplier<String>() {
			@Override
			public String get() {
				return path;
			}
		});
	}

	public String getLogFileExtension() {
		return logFileExtension;
	}

	public void setLogFileExtension(final String logFileExtension) {
		this.logFileExtension = logFileExtension;
	}

	public String getLockFileExtension() {
		return lockFileExtension;
	}

	public void setLockFileExtension(final String lockFileExtension) {
		this.lockFileExtension = lockFileExtension;
	}

}
