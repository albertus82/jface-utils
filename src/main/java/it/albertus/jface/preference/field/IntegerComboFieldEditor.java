package it.albertus.jface.preference.field;

import it.albertus.jface.Resources;
import it.albertus.jface.preference.field.listener.LowercaseVerifyListener;

import org.eclipse.swt.widgets.Composite;

public class IntegerComboFieldEditor extends ValidatedComboFieldEditor {

	protected static final int DEFAULT_TEXT_LIMIT = Integer.toString(Integer.MAX_VALUE).length() - 1;

	public IntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);

		// Compute text limit & error message...
		int length = DEFAULT_TEXT_LIMIT;
		for (final String entry[] : entryNamesAndValues) {
			if (entry[0].length() > length) {
				length = entry[0].length();
			}
		}
		if (length > DEFAULT_TEXT_LIMIT) {
			setErrorMessage(Resources.get("err.preferences.integer.range", 0, Integer.MAX_VALUE));
		}
		else {
			setErrorMessage(Resources.get("err.preferences.integer"));
		}
		getComboBoxControl().setTextLimit(length);

		getComboBoxControl().addVerifyListener(new LowercaseVerifyListener());
	}

	@Override
	protected boolean checkState() {
		if (getValue() != null) {
			try {
				final int number = Integer.parseInt(getValue());
				if (number >= 0 && number <= Integer.MAX_VALUE) {
					return true;
				}
			}
			catch (final NumberFormatException nfe) {}
		}
		return false;
	}

	@Override
	protected String getDefaultValue() {
		return Integer.toString(getPreferenceStore().getDefaultInt(getPreferenceName()));
	}

	/** Trims value and tries to convert it to integer (removes trailing zeros). */
	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = Integer.valueOf(value).toString();
		}
		catch (final Exception exception) {}
		return value;
	}

	/** Trims combo text and converts it to integer (removes trailing zeros). */
	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim().toLowerCase();
		try {
			newText = getNameForValue(Integer.valueOf(newText).toString());
		}
		catch (final Exception exception) {}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return Integer.valueOf(super.getValue()).toString();
		}
		catch (final Exception exception) {
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Integer.valueOf(value).toString());
		}
		catch (final Exception exception) {
			super.setValue(value);
		}
	}

}
