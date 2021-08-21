package it.albertus.jface.preference.field;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

public class BigIntegerComboFieldEditor extends AbstractIntegerComboFieldEditor<BigInteger> {

	private static final Logger log = LoggerFactory.getLogger(BigIntegerComboFieldEditor.class);

	public BigIntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected String cleanValue(final String value) {
		final String cleanValue = super.cleanValue(value);
		try {
			return new BigInteger(cleanValue).toString();
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided is not a valid representation of a BigInteger:", e);
			return cleanValue;
		}
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(new BigInteger(newText).toString());
		}
		catch (final Exception e) {
			log.log(Level.FINER, "Cannot determine a name for the value provided:", e);
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
			log.log(Level.FINE, "Cannot translate the string into a BigInteger:", e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(new BigInteger(value).toString());
		}
		catch (final Exception e) {
			log.log(Level.FINE, "Cannot translate the string into a BigInteger:", e);
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
				log.log(Level.FINE, "Cannot translate the string into a BigInteger:", e);
				comboValue = entry[1];
			}
			if (value.equals(comboValue)) {
				return entry[0];
			}
		}
		return value; // Name not present in the array.
	}

	@Override
	public BigInteger getNumberValue() {
		return new BigInteger(getValue());
	}

}
