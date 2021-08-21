package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

public class LongComboFieldEditor extends AbstractIntegerComboFieldEditor<Long> {

	private static final Logger log = LoggerFactory.getLogger(LongComboFieldEditor.class);

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
	protected String cleanValue(final String value) {
		final String cleanValue = super.cleanValue(value);
		try {
			return Long.valueOf(cleanValue).toString();
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable long:", e);
			return cleanValue;
		}
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		try {
			newText = getNameForValue(Long.valueOf(newText).toString());
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
			return Long.valueOf(super.getValue()).toString();
		}
		catch (final Exception e) {
			log.log(Level.FINE, "Cannot translate the string into a Long:", e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Long.valueOf(value).toString());
		}
		catch (final Exception e) {
			log.log(Level.FINE, "Cannot translate the string into a Long:", e);
			super.setValue(value);
		}
	}

	@Override
	public Long getNumberValue() {
		return Long.valueOf(getValue());
	}

}
