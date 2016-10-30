package it.albertus.jface.preference.field;

import org.eclipse.swt.widgets.Composite;

public class LongComboFieldEditor extends AbstractIntegerComboFieldEditor<Long> {

	private static final int DEFAULT_TEXT_LIMIT = Long.toString(Long.MAX_VALUE).length() - 1;

	public LongComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setMinValidValue(Long.valueOf(0L)); // Positive by default
	}

	@Override
	protected int getDefaultTextLimit() {
		return DEFAULT_TEXT_LIMIT;
	}

	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = Long.valueOf(value).toString();
		}
		catch (final Exception exception) {}
		return value;
	}

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
	public Long getNumberValue() throws NumberFormatException {
		return Long.valueOf(getValue());
	}

}
