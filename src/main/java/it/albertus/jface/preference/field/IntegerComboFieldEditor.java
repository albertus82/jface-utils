package it.albertus.jface.preference.field;

import org.eclipse.swt.widgets.Composite;

public class IntegerComboFieldEditor extends AbstractIntegerComboFieldEditor<Integer> {

	private static final int DEFAULT_TEXT_LIMIT = Integer.toString(Integer.MAX_VALUE).length();

	public IntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected int getDefaultTextLimit() {
		return DEFAULT_TEXT_LIMIT;
	}

	@Override
	protected boolean doCheckState() {
		try {
			final Integer number = Integer.valueOf(getValue());
			return checkValidRange(number);
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
		String newText = oldText.trim();

		try {
			newText = getNameForValue(Integer.valueOf(newText).toString());
		}
		catch (final Exception exception) {/* Ignore */}
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

	@Override
	public Integer getNumberValue() throws NumberFormatException {
		return Integer.valueOf(getValue());
	}

}
