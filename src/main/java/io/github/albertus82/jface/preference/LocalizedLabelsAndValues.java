package io.github.albertus82.jface.preference;

import java.util.ArrayList;
import java.util.List;

import io.github.albertus82.util.ISupplier;

public class LocalizedLabelsAndValues implements LabelsAndValues {

	private final List<LabelValue> entries;

	public LocalizedLabelsAndValues() {
		entries = new ArrayList<>();
	}

	public LocalizedLabelsAndValues(final int initialCapacity) {
		entries = new ArrayList<>(initialCapacity);
	}

	public LocalizedLabelsAndValues(final ISupplier<String> name, final Object value) {
		this(1);
		add(name, value);
	}

	public void add(final ISupplier<String> name, final Object value) {
		entries.add(new LabelValue(name, String.valueOf(value)));
	}

	@Override
	public String[][] toArray() {
		final String[][] options = new String[entries.size()][2];
		int index = 0;
		for (final LabelValue entry : entries) {
			options[index][0] = entry.key.get();
			options[index][1] = entry.value;
			index++;
		}
		return options;
	}

	@Override
	public String toString() {
		return entries.toString();
	}

	private class LabelValue {
		private final ISupplier<String> key;
		private final String value;

		private LabelValue(final ISupplier<String> key, final String value) {
			if (key == null) {
				throw new NullPointerException("key cannot be null");
			}
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key.get() + "=" + value;
		}
	}

}
