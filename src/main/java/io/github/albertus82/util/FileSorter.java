package io.github.albertus82.util;

import java.io.File;
import java.util.Arrays;

public class FileSorter {

	private FileSorter() {
		throw new IllegalAccessError("Utility class");
	}

	public static void sortByLastModified(final File[] files) {
		final Pair[] pairs = new Pair[files.length];
		for (int i = 0; i < files.length; i++) {
			pairs[i] = new Pair(files[i]);
		}
		Arrays.sort(pairs);
		for (int i = 0; i < files.length; i++) {
			files[i] = pairs[i].getFile();
		}
	}

}

class Pair implements Comparable<Pair> {

	private final long time;
	private final File file;

	Pair(final File file) {
		this.file = file;
		this.time = file.lastModified();
	}

	@Override
	public int compareTo(final Pair other) {
		return this.time < other.time ? -1 : this.time == other.time ? 0 : 1;
	}

	long getTime() {
		return time;
	}

	File getFile() {
		return file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
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
		if (!(obj instanceof Pair)) {
			return false;
		}
		Pair other = (Pair) obj;
		if (file == null) {
			if (other.file != null) {
				return false;
			}
		}
		else if (!file.equals(other.file)) {
			return false;
		}
		if (time != other.time) {
			return false;
		}
		return true;
	}

}
