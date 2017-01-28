package it.albertus.jface.preference.field;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

public class BigIntegerComboFieldEditor extends AbstractIntegerComboFieldEditor<BigInteger> {

	private static final Logger logger = LoggerFactory.getLogger(BigIntegerComboFieldEditor.class);

	public BigIntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = new BigInteger(value).toString();
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
		}
		return value;
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(new BigInteger(newText).toString());
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
		}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return new BigInteger(super.getValue()).toString();
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(new BigInteger(value).toString());
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
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
				logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
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
	public BigInteger getNumberValue() throws NumberFormatException {
		return new BigInteger(getValue());
	}

}
