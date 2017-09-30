package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

public class IntegerComboFieldEditor extends AbstractIntegerComboFieldEditor<Integer> {

	private static final Logger logger = LoggerFactory.getLogger(IntegerComboFieldEditor.class);

	private static final int DEFAULT_TEXT_LIMIT = Integer.toString(Integer.MAX_VALUE).length() - 1;

	public IntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setMinValidValue(Integer.valueOf(0)); // Positive by default
	}

	@Override
	protected int getDefaultTextLimit() {
		return DEFAULT_TEXT_LIMIT;
	}

	@Override
	protected String cleanValue(final String value) {
		final String cleanValue = super.cleanValue(value);
		try {
			return Integer.valueOf(cleanValue).toString();
		}
		catch (final NumberFormatException nfe) {
			return cleanValue;
		}
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(Integer.valueOf(newText).toString());
		}
		catch (final Exception e) {
			logger.log(Level.FINER, e.toString(), e);
		}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return Integer.valueOf(super.getValue()).toString();
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.toString(), e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Integer.valueOf(value).toString());
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.toString(), e);
			super.setValue(value);
		}
	}

	@Override
	public Integer getNumberValue() {
		return Integer.valueOf(getValue());
	}

}
