package it.albertus.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

public class PropertiesComparator {

	private PropertiesComparator() {
		throw new IllegalAccessError();
	}

	public static class CompareResults {

		private final Properties leftOnly;
		private final Properties rightOnly;
		private final Map<String, List<String>> differentValues;

		public CompareResults(final Properties leftOnly, final Properties rightOnly, final Map<String, List<String>> differentValues) {
			this.leftOnly = leftOnly;
			this.rightOnly = rightOnly;
			this.differentValues = differentValues;
		}

		public Properties getLeftOnly() {
			return leftOnly;
		}

		public Properties getRightOnly() {
			return rightOnly;
		}

		public Map<String, List<String>> getDifferentValues() {
			return differentValues;
		}

	}

	public static CompareResults compare(final Properties left, final Properties right) {
		final Set<String> leftKeys = left.stringPropertyNames();
		final Set<String> rightKeys = right.stringPropertyNames();

		final Set<String> removedKeys = new HashSet<String>(leftKeys);
		removedKeys.removeAll(rightKeys);
		final Properties leftOnly = new Properties();
		for (final String removedKey : removedKeys) {
			leftOnly.setProperty(removedKey, left.getProperty(removedKey));
		}

		final Set<String> addedKeys = new HashSet<String>(rightKeys);
		addedKeys.removeAll(leftKeys);
		final Properties rightOnly = new Properties();
		for (final String addedKey : addedKeys) {
			rightOnly.setProperty(addedKey, right.getProperty(addedKey));
		}

		final Set<String> commonKeys = new HashSet<String>(leftKeys);
		commonKeys.retainAll(rightKeys);
		final Set<String> modifiedKeys = new HashSet<String>();
		for (final String commonkey : commonKeys) {
			if (!left.getProperty(commonkey).equals(right.getProperty(commonkey))) {
				modifiedKeys.add(commonkey);
			}
		}
		final Map<String, List<String>> differentValues = new TreeMap<String, List<String>>();
		for (final String modifiedKey : modifiedKeys) {
			final ArrayList<String> values = new ArrayList<String>(2);
			values.add(left.getProperty(modifiedKey));
			values.add(right.getProperty(modifiedKey));
			differentValues.put(modifiedKey, Collections.unmodifiableList(values));
		}
		return new CompareResults(leftOnly, rightOnly, differentValues);
	}

}
