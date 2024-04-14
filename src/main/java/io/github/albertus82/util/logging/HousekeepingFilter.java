package io.github.albertus82.util.logging;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import io.github.albertus82.jface.JFaceMessages;
import io.github.albertus82.util.FileSorter;

public class HousekeepingFilter extends Observable implements Filter {

	private static final Logger log = LoggerFactory.getLogger(HousekeepingFilter.class);

	public static final int MIN_HISTORY = 1;

	private final ILogFileManager logFileManager;
	private final int maxHistory;
	private final String datePattern;

	private String currentFileNamePart;

	public HousekeepingFilter(final ILogFileManager logFileManager, final int maxHistory) {
		this(logFileManager, maxHistory, TimeBasedRollingFileHandlerConfig.DEFAULT_DATE_PATTERN);
	}

	public HousekeepingFilter(final ILogFileManager logFileManager, final int maxHistory, final String datePattern) {
		this.logFileManager = logFileManager;
		if (maxHistory < MIN_HISTORY) {
			log.log(Level.WARNING, JFaceMessages.get("err.logging.housekeeping.maxHistory"), MIN_HISTORY);
			this.maxHistory = MIN_HISTORY;
		}
		else {
			this.maxHistory = maxHistory;
		}
		new SimpleDateFormat(datePattern); // Enforce pattern validity
		this.datePattern = datePattern;
		log.log(Level.FINE, "Created new {0}", this);
	}

	@Override
	public boolean isLoggable(final LogRecord rec) {
		final String newFileNamePart = new SimpleDateFormat(datePattern).format(new Date());
		if (!newFileNamePart.equals(currentFileNamePart)) {
			int keep = this.maxHistory;
			if (currentFileNamePart == null) {
				keep++;
			}
			currentFileNamePart = newFileNamePart;
			deleteOldLogs(keep);
		}
		return true;
	}

	private void deleteOldLogs(final int keep) {
		final File[] files = logFileManager.listFiles();
		if (files != null && files.length > keep) {
			FileSorter.sortByLastModified(files);
			for (int i = 0; i < files.length - keep; i++) {
				final boolean deleted = logFileManager.deleteFile(files[i]);
				if (deleted) {
					setChanged();
					notifyObservers(files[i]);
				}
			}
		}
	}

	public int getMaxHistory() {
		return maxHistory;
	}

	public String getDatePattern() {
		return datePattern;
	}

	@Override
	public String toString() {
		return "HousekeepingFilter [maxHistory=" + maxHistory + ", datePattern=" + datePattern + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datePattern == null) ? 0 : datePattern.hashCode());
		result = prime * result + maxHistory;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HousekeepingFilter)) {
			return false;
		}
		HousekeepingFilter other = (HousekeepingFilter) obj;
		if (datePattern == null) {
			if (other.datePattern != null) {
				return false;
			}
		}
		else if (!datePattern.equals(other.datePattern)) {
			return false;
		}
		if (maxHistory != other.maxHistory) {
			return false;
		}
		return true;
	}

}
