package it.albertus.jface.preference.field;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

public class BigDecimalComboFieldEditor extends AbstractDecimalComboFieldEditor<BigDecimal> {

	private static final Logger logger = LoggerFactory.getLogger(BigDecimalComboFieldEditor.class);

	public BigDecimalComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = new BigDecimal(value).toString();
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
			newText = getNameForValue(new BigDecimal(newText).toString());
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
			return new BigDecimal(super.getValue()).toString();
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(new BigDecimal(value).toString());
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
				comboValue = new BigDecimal(entry[1]).toString();
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
			defaultValue = new BigDecimal(defaultValue).toString();
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return defaultValue;
	}

	@Override
	public BigDecimal getNumberValue() throws NumberFormatException {
		return new BigDecimal(getValue());
	}

}
