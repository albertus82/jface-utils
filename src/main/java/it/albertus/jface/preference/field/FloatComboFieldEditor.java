package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.swt.widgets.Composite;

public class FloatComboFieldEditor extends NumberComboFieldEditor {

	private static final int DEFAULT_TEXT_LIMIT = 16;

	private float minValidValue = Float.NEGATIVE_INFINITY;
	private float maxValidValue = Float.POSITIVE_INFINITY;

	public FloatComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setTextLimit(Math.max(getMaxLabelLength(), DEFAULT_TEXT_LIMIT));
		JFaceMessages.get("err.preferences.decimal");
	}

	@Override
	protected boolean doCheckState() {
		try {
			final float number = Float.parseFloat(getValue());
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
			value = Float.valueOf(value).toString();
		}
		catch (final Exception exception) {}
		return value;
	}

	/** Trims combo text and converts it to integer (removes trailing zeros). */
	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(Float.valueOf(newText).toString());
		}
		catch (final Exception exception) {/* Ignore */}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return Float.valueOf(super.getValue()).toString();
		}
		catch (final Exception exception) {
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Float.valueOf(value).toString());
		}
		catch (final Exception exception) {
			super.setValue(value);
		}
	}

	@Override
	protected String getNameForValue(final String value) {
		for (final String[] entry : getEntryNamesAndValues()) {
			if (value.equals(Float.valueOf(entry[1]).toString())) {
				return entry[0];
			}
		}
		return value; // Name not present in the array.
	}

	@Override
	protected String getDefaultValue() {
		String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		try {
			defaultValue = Float.toString(Float.parseFloat(defaultValue));
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return defaultValue;
	}

	public void setValidRange(final float min, final float max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceMessages.get("err.preferences.decimal.range", min, max));
	}

	public float getMinValidValue() {
		return minValidValue;
	}

	public float getMaxValidValue() {
		return maxValidValue;
	}

}
