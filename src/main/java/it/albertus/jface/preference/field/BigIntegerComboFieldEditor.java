package it.albertus.jface.preference.field;

import java.math.BigInteger;

import org.eclipse.swt.widgets.Composite;

public class BigIntegerComboFieldEditor extends NumberComboFieldEditor {

	public BigIntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected boolean doCheckState() {
		try {
			final BigInteger number = new BigInteger(getValue());
			return checkValidRange(number);
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

	@Override
	public BigInteger getMinValidValue() {
		return (BigInteger) super.getMinValidValue();
	}

	@Override
	public void setMinValidValue(final Number min) {
		if (min == null || min instanceof BigInteger) {
			super.setMinValidValue((BigInteger) min);
		}
		else {
			super.setMinValidValue(BigInteger.valueOf(min.longValue()));
		}
	}

	@Override
	public BigInteger getMaxValidValue() {
		return (BigInteger) super.getMaxValidValue();
	}

	@Override
	public void setMaxValidValue(final Number max) {
		if (max == null || max instanceof BigInteger) {
			super.setMaxValidValue((BigInteger) max);
		}
		else {
			super.setMaxValidValue(BigInteger.valueOf(max.longValue()));
		}
	}

	@Override
	protected NumberType getNumberType() {
		return NumberType.INTEGER;
	}

	public BigInteger getBigIntegerValue() throws NumberFormatException {
		return new BigInteger(getValue());
	}

}
