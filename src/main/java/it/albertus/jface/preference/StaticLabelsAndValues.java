package it.albertus.jface.preference;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import it.albertus.util.MapUtils;

public class StaticLabelsAndValues implements LabelsAndValues {

	private final Map<String, String> entries;

	public StaticLabelsAndValues() {
		entries = new LinkedHashMap<String, String>();
	}

	public StaticLabelsAndValues(final int expectedSize) {
		entries = MapUtils.<String, String>newLinkedHashMapWithExpectedSize(expectedSize);
	}

	public StaticLabelsAndValues(final String name, final Object value) {
		this(1);
		put(name, value);
	}

	public StaticLabelsAndValues put(final String name, final Object value) {
		entries.put(name, String.valueOf(value));
		return this;
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
