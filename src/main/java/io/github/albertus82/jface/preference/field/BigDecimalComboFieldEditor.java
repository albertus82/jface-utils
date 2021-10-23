package io.github.albertus82.jface.preference.field;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import io.github.albertus82.util.logging.LoggerFactory;

public class BigDecimalComboFieldEditor extends AbstractDecimalComboFieldEditor<BigDecimal> {

	private static final Logger log = LoggerFactory.getLogger(BigDecimalComboFieldEditor.class);

	public BigDecimalComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected String cleanValue(final String value) {
		final String cleanValue = super.cleanValue(value);
		try {
			return new BigDecimal(cleanValue).toString();
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided is not a valid representation of a BigDecimal:", e);
			return cleanValue;
		}
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(new BigDecimal(newText).toString());
		}
		catch (final Exception e) {
			log.log(Level.FINEST, "Cannot determine a name for the value provided:", e);
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
			log.log(Level.FINEST, "Cannot translate the string into a BigDecimal:", e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(new BigDecimal(value).toString());
		}
		catch (final Exception e) {
			log.log(Level.FINEST, "Cannot translate the string into a BigDecimal:", e);
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
				log.log(Level.FINE, "Cannot translate the string into a BigDecimal:", e);
				comboValue = entry[1];
			}
			if (value.equals(comboValue)) {
				return entry[0];
			}
		}
		return value; // Name not present in the array.
	}

	@Override
	public BigDecimal getNumberValue() {
		return new BigDecimal(getValue());
	}

}
