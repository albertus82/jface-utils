package it.albertus.jface.preference.field;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Composite;

public class BigDecimalComboFieldEditor extends NumberComboFieldEditor {

	public BigDecimalComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected boolean doCheckState() {
		try {
			final BigDecimal number = new BigDecimal(getValue());
			return checkValidRange(number);
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

	@Override
	public BigDecimal getMinValidValue() {
		return (BigDecimal) super.getMinValidValue();
	}

	@Override
	public void setMinValidValue(final Number min) {
		if (min == null || min instanceof BigDecimal) {
			super.setMinValidValue((BigDecimal) min);
		}
		else {
			super.setMinValidValue(BigDecimal.valueOf(min.doubleValue()));
		}
	}

	@Override
	public BigDecimal getMaxValidValue() {
		return (BigDecimal) super.getMaxValidValue();
	}

	@Override
	public void setMaxValidValue(final Number max) {
		if (max == null || max instanceof BigDecimal) {
			super.setMaxValidValue((BigDecimal) max);
		}
		else {
			super.setMaxValidValue(BigDecimal.valueOf(max.doubleValue()));
		}
	}

	@Override
	protected NumberType getNumberType() {
		return NumberType.DECIMAL;
	}

	public BigDecimal getBigDecimalValue() throws NumberFormatException {
		return new BigDecimal(getValue());
	}

}
