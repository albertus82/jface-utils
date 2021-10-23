package io.github.albertus82.util.logging;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import io.github.albertus82.util.ISupplier;
import io.github.albertus82.util.Supplier;

public class LogFileManager implements ILogFileManager {

	public static final String DEFAULT_LOG_FILE_EXTENSION = ".log";
	public static final String DEFAULT_LOCK_FILE_EXTENSION = ".lck";

	private ISupplier<String> path;

	private String logFileExtension = DEFAULT_LOG_FILE_EXTENSION;
	private String lockFileExtension = DEFAULT_LOCK_FILE_EXTENSION;

	private FilenameFilter logFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
			return name != null && (name.toLowerCase().contains(getLogFileExtension()) || name.toLowerCase(Locale.ROOT).contains(getLogFileExtension())) && !name.endsWith(getLockFileExtension());
		}
	};
	private FilenameFilter lockFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
			return name != null && name.endsWith(getLockFileExtension());
		}
	};

	public LogFileManager(final ISupplier<String> path) {
		this.path = path;
	}

	public LogFileManager(final String path) {
		this.path = new Supplier<String>() {
			@Override
			public String get() {
				return path;
			}
		};
	}

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

	/**
	 * Returns the directory that is configured as the destination path for the
	 * application's log files.
	 * 
	 * @return the directory that contains the application's logs.
	 */
	public String getPath() {
		return path.get();
	}

	public void setPath(final ISupplier<String> path) {
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

	public FilenameFilter getLogFilenameFilter() {
		return logFilenameFilter;
	}

	public void setLogFilenameFilter(final FilenameFilter logFilenameFilter) {
		this.logFilenameFilter = logFilenameFilter;
	}

	public FilenameFilter getLockFilenameFilter() {
		return lockFilenameFilter;
	}

	public void setLockFilenameFilter(final FilenameFilter lockFilenameFilter) {
		this.lockFilenameFilter = lockFilenameFilter;
	}

}
