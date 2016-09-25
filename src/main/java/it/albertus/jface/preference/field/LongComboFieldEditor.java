package it.albertus.jface.preference.field;

import org.eclipse.swt.widgets.Composite;

public class LongComboFieldEditor extends NumberComboFieldEditor {

	private static final int DEFAULT_TEXT_LIMIT = Long.toString(Long.MAX_VALUE).length();

	public LongComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected int getDefaultTextLimit() {
		return DEFAULT_TEXT_LIMIT;
	}

	@Override
	protected boolean doCheckState() {
		try {
			final long number = Long.parseLong(getValue());
			return super.checkValidRange(number);
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

	@Override
	protected NumberType getNumberType() {
		return NumberType.INTEGER;
	}

	@Override
	public Long getMinValidValue() {
		return (Long) super.getMinValidValue();
	}

	@Override
	public void setMinValidValue(final Number min) {
		super.setMinValidValue(min != null ? min.longValue() : null);
	}

	@Override
	public Long getMaxValidValue() {
		return (Long) super.getMaxValidValue();
	}

	@Override
	public void setMaxValidValue(final Number max) {
		super.setMaxValidValue(max != null ? max.longValue() : null);
	}

	public Long getLongValue() throws NumberFormatException {
		return Long.valueOf(getValue());
	}

}
