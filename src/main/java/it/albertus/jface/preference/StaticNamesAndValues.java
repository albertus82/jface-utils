package it.albertus.jface.preference;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StaticNamesAndValues implements NamesAndValues {

	private final Map<String, String> entries;

	public StaticNamesAndValues() {
		entries = new LinkedHashMap<String, String>();
	}

	public StaticNamesAndValues(final int initialCapacity) {
		entries = new LinkedHashMap<String, String>(initialCapacity);
	}

	public StaticNamesAndValues(final String name, final Object value) {
		this(1);
		put(name, value);
	}

	public void put(final String name, final Object value) {
		entries.put(name, String.valueOf(value));
	}

	@Override
	public String[][] toArray() {
		final String[][] options = new String[entries.size()][2];
		int index = 0;
		for (final Entry<String, String> entry : entries.entrySet()) {
			options[index][0] = entry.getKey();
			options[index][1] = entry.getValue();
			index++;
		}
		return options;
	}

	@Override
	public String toString() {
		return entries.toString();
	}

}
