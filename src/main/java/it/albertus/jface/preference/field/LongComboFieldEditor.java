package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

public class LongComboFieldEditor extends AbstractIntegerComboFieldEditor<Long> {

	private static final Logger logger = LoggerFactory.getLogger(LongComboFieldEditor.class);

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
			newText = getNameForValue(Long.valueOf(newText).toString());
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
			return Long.valueOf(super.getValue()).toString();
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Long.valueOf(value).toString());
		}
		catch (final Exception e) {
			logger.log(Level.FINE, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage(), e);
			super.setValue(value);
		}
	}

	@Override
	public Long getNumberValue() throws NumberFormatException {
		return Long.valueOf(getValue());
	}

}
