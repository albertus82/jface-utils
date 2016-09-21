package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.listener.LowercaseVerifyListener;

import org.eclipse.swt.widgets.Composite;

public class IntegerComboFieldEditor extends ValidatedComboFieldEditor {

	protected static final int DEFAULT_TEXT_LIMIT = Integer.toString(Integer.MAX_VALUE).length() - 1;

	private int minValidValue = 0;
	private int maxValidValue = Integer.MAX_VALUE;

	public IntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);

		// Compute text limit & error message...
		final int length = getMaxLabelLength();
		if (length > DEFAULT_TEXT_LIMIT) {
			setErrorMessage(JFaceMessages.get("err.preferences.integer.range", minValidValue, maxValidValue));
		}
		else {
			setErrorMessage(JFaceMessages.get("err.preferences.integer"));
		}
		setTextLimit(length);

		getComboBoxControl().addVerifyListener(new LowercaseVerifyListener());
	}

	@Override
	protected boolean doCheckState() {
		try {
			final int number = Integer.parseInt(getValue());
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

	public void setValidRange(final int min, final int max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceMessages.get("err.preferences.integer.range", min, max));
	}

	public int getMinValidValue() {
		return minValidValue;
	}

	public int getMaxValidValue() {
		return maxValidValue;
	}

}
