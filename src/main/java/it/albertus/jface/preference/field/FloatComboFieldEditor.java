package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

public class FloatComboFieldEditor extends AbstractDecimalComboFieldEditor<Float> {

	private static final Logger log = LoggerFactory.getLogger(FloatComboFieldEditor.class);

	private static final int DEFAULT_TEXT_LIMIT = 16;

	public FloatComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected int getDefaultTextLimit() {
		return DEFAULT_TEXT_LIMIT;
	}

	@Override
	protected String cleanValue(final String value) {
		final String cleanValue = super.cleanValue(value);
		try {
			return Float.valueOf(cleanValue).toString();
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable float:", e);
			return cleanValue;
		}
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(Float.valueOf(newText).toString());
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
			return Float.valueOf(super.getValue()).toString();
		}
		catch (final Exception e) {
			log.log(Level.FINE, "Cannot translate the string into a Float:", e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Float.valueOf(value).toString());
		}
		catch (final Exception e) {
			log.log(Level.FINE, "Cannot translate the string into a Float:", e);
			super.setValue(value);
		}
	}

	@Override
	protected String getNameForValue(final String value) {
		for (final String[] entry : getEntryNamesAndValues()) {
			String comboValue;
			try {
				comboValue = Float.valueOf(entry[1]).toString();
			}
			catch (final Exception e) {
				log.log(Level.FINE, "Cannot translate the string into a Float:", e);
				comboValue = entry[1];
			}
			if (value.equals(comboValue)) {
				return entry[0];
			}
		}
		return value; // Name not present in the array.
	}

	@Override
	public Float getNumberValue() {
		return Float.valueOf(getValue());
	}

}
