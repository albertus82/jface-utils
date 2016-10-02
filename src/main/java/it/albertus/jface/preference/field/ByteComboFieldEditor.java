package it.albertus.jface.preference.field;

import org.eclipse.swt.widgets.Composite;

public class ByteComboFieldEditor extends AbstractIntegerComboFieldEditor<Byte> {

	private static final int DEFAULT_TEXT_LIMIT = Byte.toString(Byte.MIN_VALUE).length();

	public ByteComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setMinValidValue((byte) 0); // Positive by default
	}

	@Override
	protected int getDefaultTextLimit() {
		return DEFAULT_TEXT_LIMIT;
	}

	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = Byte.valueOf(value).toString();
		}
		catch (final Exception exception) {}
		return value;
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(Byte.valueOf(newText).toString());
		}
		catch (final Exception exception) {/* Ignore */}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return Byte.valueOf(super.getValue()).toString();
		}
		catch (final Exception exception) {
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Byte.valueOf(value).toString());
		}
		catch (final Exception exception) {
			super.setValue(value);
		}
	}

	@Override
	public Byte getNumberValue() throws NumberFormatException {
		return Byte.valueOf(getValue());
	}

}
