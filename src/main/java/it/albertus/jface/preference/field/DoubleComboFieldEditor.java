package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

public class DoubleComboFieldEditor extends AbstractDecimalComboFieldEditor<Double> {

	private static final Logger logger = LoggerFactory.getLogger(DoubleComboFieldEditor.class);

	private static final int DEFAULT_TEXT_LIMIT = 32;

	public DoubleComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected int getDefaultTextLimit() {
		return DEFAULT_TEXT_LIMIT;
	}

	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = Double.valueOf(value).toString();
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
			newText = getNameForValue(Double.valueOf(newText).toString());
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
			return Double.valueOf(super.getValue()).toString();
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Double.valueOf(value).toString());
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
				comboValue = Double.valueOf(entry[1]).toString();
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
			defaultValue = Double.toString(Double.parseDouble(defaultValue));
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return defaultValue;
	}

	@Override
	public Double getNumberValue() throws NumberFormatException {
		return Double.valueOf(getValue());
	}

}
