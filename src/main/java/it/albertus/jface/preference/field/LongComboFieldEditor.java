package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.swt.widgets.Composite;

public class LongComboFieldEditor extends NumberComboFieldEditor {

	private static final int DEFAULT_TEXT_LIMIT = Long.toString(Long.MAX_VALUE).length() - 1;

	private long minValidValue = 0;
	private long maxValidValue = Long.MAX_VALUE;

	public LongComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);

		// Compute text limit & error message...
		final int maxLabelLength = getMaxLabelLength();
		if (maxLabelLength > DEFAULT_TEXT_LIMIT) {
			setErrorMessage(JFaceMessages.get("err.preferences.integer.range", minValidValue, maxValidValue));
		}
		else {
			setErrorMessage(JFaceMessages.get("err.preferences.integer"));
		}
		setTextLimit(Math.max(maxLabelLength, DEFAULT_TEXT_LIMIT));
	}

	@Override
	protected boolean doCheckState() {
		try {
			final long number = Long.parseLong(getValue());
			if (number >= minValidValue && number <= maxValidValue) {
				return true;
			}
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return false;
	}

	/** Trims value and tries to convert it to long (removes trailing zeros). */
	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = Long.valueOf(value).toString();
		}
		catch (final Exception exception) {}
		return value;
	}

	/** Trims combo text and converts it to long (removes trailing zeros). */
	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		switch (labelsCase) {
		case UPPER:
			newText = newText.toUpperCase();
			break;
		case LOWER:
			newText = newText.toLowerCase();
			break;
		}

		try {
			newText = getNameForValue(Long.valueOf(newText).toString());
		}
		catch (final Exception exception) {/* Ignore */}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return Long.valueOf(super.getValue()).toString();
		}
		catch (final Exception exception) {
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Long.valueOf(value).toString());
		}
		catch (final Exception exception) {
			super.setValue(value);
		}
	}

	public void setValidRange(final long min, final long max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceMessages.get("err.preferences.integer.range", min, max));
	}

	public long getMinValidValue() {
		return minValidValue;
	}

	public long getMaxValidValue() {
		return maxValidValue;
	}

}
