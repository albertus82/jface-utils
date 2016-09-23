package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.swt.widgets.Composite;

public class DoubleComboFieldEditor extends NumberComboFieldEditor {

	private static final int DEFAULT_TEXT_LIMIT = 32;

	private double minValidValue = Double.NEGATIVE_INFINITY;
	private double maxValidValue = Double.POSITIVE_INFINITY;

	public DoubleComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setTextLimit(Math.max(getMaxLabelLength(), DEFAULT_TEXT_LIMIT));
		JFaceMessages.get("err.preferences.decimal");
	}

	@Override
	protected boolean doCheckState() {
		try {
			final double number = Double.parseDouble(getValue());
			if (number >= minValidValue && number <= maxValidValue) {
				return true;
			}
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return false;
	}

	/** Trims value and tries to convert it to integer (removes trailing zeros). */
	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = Double.valueOf(value).toString();
		}
		catch (final Exception exception) {/* Ignore */}
		return value;
	}

	/** Trims combo text and converts it to integer (removes trailing zeros). */
	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(Double.valueOf(newText).toString());
		}
		catch (final Exception exception) {/* Ignore */}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return Double.valueOf(super.getValue()).toString();
		}
		catch (final Exception exception) {
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Double.valueOf(value).toString());
		}
		catch (final Exception exception) {
			super.setValue(value);
		}
	}

	@Override
	protected String getNameForValue(final String value) {
		for (final String[] entry : getEntryNamesAndValues()) {
			String comboValue;
			try {
				comboValue = Double.valueOf(entry[1]).toString();
			}
			catch (final Exception e) {
				comboValue = entry[1];
			}
			if (value.equals(comboValue)) {
				return entry[0];
			}
		}
		return value; // Name not present in the array.
	}

	@Override
	protected String getDefaultValue() {
		String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		try {
			defaultValue = Double.toString(Double.parseDouble(defaultValue));
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return defaultValue;
	}

	public void setValidRange(final double min, final double max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceMessages.get("err.preferences.decimal.range", min, max));
	}

	public double getMinValidValue() {
		return minValidValue;
	}

	public double getMaxValidValue() {
		return maxValidValue;
	}

}
