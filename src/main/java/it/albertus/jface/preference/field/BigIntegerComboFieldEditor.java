package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import java.math.BigInteger;
import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Composite;

public class BigIntegerComboFieldEditor extends NumberComboFieldEditor {

	private BigInteger minValidValue;
	private BigInteger maxValidValue;

	public BigIntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setTextLimit(Math.max(getMaxLabelLength(), Preferences.MAX_VALUE_LENGTH));
		JFaceMessages.get("err.preferences.integer");
	}

	@Override
	protected boolean doCheckState() {
		try {
			final BigInteger number = new BigInteger(getValue());
			if (minValidValue == null || maxValidValue == null || (number.compareTo(minValidValue) >= 0 && number.compareTo(maxValidValue) <= 0)) {
				return true;
			}
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return false;
	}

	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = new BigInteger(value).toString();
		}
		catch (final Exception exception) {/* Ignore */}
		return value;
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(new BigInteger(newText).toString());
		}
		catch (final Exception exception) {/* Ignore */}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return new BigInteger(super.getValue()).toString();
		}
		catch (final Exception exception) {
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(new BigInteger(value).toString());
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
				comboValue = new BigInteger(entry[1]).toString();
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
			defaultValue = new BigInteger(defaultValue).toString();
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return defaultValue;
	}

	public void setValidRange(final Number min, final Number max) {
		setMinValidValue(min);
		setMaxValidValue(max);
		setErrorMessage(JFaceMessages.get("err.preferences.integer.range", min, max));
	}

	protected void setMaxValidValue(final Number max) {
		if (max instanceof BigInteger) {
			maxValidValue = (BigInteger) max;
		}
		else {
			maxValidValue = BigInteger.valueOf(max.longValue());
		}
	}

	protected void setMinValidValue(final Number min) {
		if (min instanceof BigInteger) {
			minValidValue = (BigInteger) min;
		}
		else {
			minValidValue = BigInteger.valueOf(min.longValue());
		}
	}

	public BigInteger getMinValidValue() {
		return minValidValue;
	}

	public BigInteger getMaxValidValue() {
		return maxValidValue;
	}

}
