package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import java.math.BigDecimal;
import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Composite;

public class BigDecimalComboFieldEditor extends NumberComboFieldEditor {

	private BigDecimal minValidValue;
	private BigDecimal maxValidValue;

	public BigDecimalComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setTextLimit(Math.max(getMaxLabelLength(), Preferences.MAX_VALUE_LENGTH));
		JFaceMessages.get("err.preferences.decimal");
	}

	@Override
	protected boolean doCheckState() {
		try {
			final BigDecimal number = new BigDecimal(getValue());
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
			value = new BigDecimal(value).toString();
		}
		catch (final Exception exception) {/* Ignore */}
		return value;
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(new BigDecimal(newText).toString());
		}
		catch (final Exception exception) {/* Ignore */}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return new BigDecimal(super.getValue()).toString();
		}
		catch (final Exception exception) {
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(new BigDecimal(value).toString());
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
				comboValue = new BigDecimal(entry[1]).toString();
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
			defaultValue = new BigDecimal(defaultValue).toString();
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return defaultValue;
	}

	public void setValidRange(final Number min, final Number max) {
		if (min instanceof BigDecimal) {
			minValidValue = (BigDecimal) min;
		}
		else {
			minValidValue = BigDecimal.valueOf(min.doubleValue());
		}
		if (max instanceof BigDecimal) {
			maxValidValue = (BigDecimal) max;
		}
		else {
			maxValidValue = BigDecimal.valueOf(max.doubleValue());
		}
		setErrorMessage(JFaceMessages.get("err.preferences.decimal.range", min, max));
	}

	public BigDecimal getMinValidValue() {
		return minValidValue;
	}

	public BigDecimal getMaxValidValue() {
		return maxValidValue;
	}

}
