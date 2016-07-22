package it.albertus.jface.preference;

import it.albertus.util.Localized;

import java.util.ArrayList;
import java.util.List;

public class LocalizedComboEntryNamesAndValues {

	private final List<LocalizedComboEntryNameAndValue> entries = new ArrayList<LocalizedComboEntryNameAndValue>();

	public LocalizedComboEntryNamesAndValues() {}

	public LocalizedComboEntryNamesAndValues(final Localized name, final Object value) {
		add(name, value);
	}

	public void add(final Localized name, final Object value) {
		entries.add(new LocalizedComboEntryNameAndValue(name, String.valueOf(value)));
	}

	public String[][] toArray() {
		final String[][] options = new String[entries.size()][2];
		for (int index = 0; index < entries.size(); index++) {
			final LocalizedComboEntryNameAndValue entry = entries.get(index);
			options[index][0] = entry.name.getString();
			options[index][1] = entry.value;
		}
		return options;
	}

	private class LocalizedComboEntryNameAndValue {
		private final Localized name;
		private final String value;

		public LocalizedComboEntryNameAndValue(final Localized name, final String value) {
			this.name = name;
			this.value = value;
		}
	}

}
