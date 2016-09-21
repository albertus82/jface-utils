package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.listener.LowerCaseVerifyListener;
import it.albertus.jface.listener.TrimVerifyListener;
import it.albertus.jface.listener.UpperCaseVerifyListener;

import org.eclipse.swt.widgets.Composite;

public class IntegerComboFieldEditor extends ValidatedComboFieldEditor {

	protected static final int DEFAULT_TEXT_LIMIT = Integer.toString(Integer.MAX_VALUE).length() - 1;

	private int minValidValue = 0;
	private int maxValidValue = Integer.MAX_VALUE;

	protected final LabelsCase labelsCase;

	public IntegerComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		labelsCase = getLabelsCase();

		// Compute text limit & error message...
		final int length = getMaxLabelLength();
		if (length > DEFAULT_TEXT_LIMIT) {
			setErrorMessage(JFaceMessages.get("err.preferences.integer.range", minValidValue, maxValidValue));
		}
		else {
			setErrorMessage(JFaceMessages.get("err.preferences.integer"));
		}
		setTextLimit(length);

		// If all the labels are upper or lower case, force the input case.
		switch (labelsCase) {
		case UPPER:
			getComboBoxControl().addVerifyListener(new UpperCaseVerifyListener());
			break;
		case LOWER:
			getComboBoxControl().addVerifyListener(new LowerCaseVerifyListener());
			break;
		}

		// If none of the labels contain whitespace, disable the space bar.
		if (!labelsContainWhitespace()) {
			getComboBoxControl().addVerifyListener(new TrimVerifyListener());
		}
	}

	@Override
	protected boolean doCheckState() {
		try {
			final int number = Integer.parseInt(getValue());
			if (number >= minValidValue && number <= maxValidValue) {
				return true;
			}
		}
		catch (final NumberFormatException nfe) {/* Ignore */}
		return false;
	}

	/** Trims value and tries to convert it to integer (removes trailing zeros). */
	@Override
	protected String cleanValue(String value) {
		value = super.cleanValue(value);
		try {
			value = Integer.valueOf(value).toString();
		}
		catch (final Exception exception) {}
		return value;
	}

	/** Trims combo text and converts it to integer (removes trailing zeros). */
	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		String newText = oldText.trim();

		switch (labelsCase) {
		case UPPER:
			newText = newText.toUpperCase();
			break;
		case LOWER:
			newText = newText.toLowerCase();
			break;
		}

		try {
			newText = getNameForValue(Integer.valueOf(newText).toString());
		}
		catch (final Exception exception) {}
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	@Override
	public String getValue() {
		try {
			return Integer.valueOf(super.getValue()).toString();
		}
		catch (final Exception exception) {
			return super.getValue();
		}
	}

	@Override
	protected void setValue(final String value) {
		try {
			super.setValue(Integer.valueOf(value).toString());
		}
		catch (final Exception exception) {
			super.setValue(value);
		}
	}

	public void setValidRange(final int min, final int max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceMessages.get("err.preferences.integer.range", min, max));
	}

	public int getMinValidValue() {
		return minValidValue;
	}

	public int getMaxValidValue() {
		return maxValidValue;
	}

	protected boolean labelsContainWhitespace() {
		for (final String entry[] : getEntryNamesAndValues()) {
			if (entry[0].contains(" ")) {
				return true;
			}
		}
		return false;
	}

	protected LabelsCase getLabelsCase() {
		int upperCaseCount = 0;
		int lowerCaseCount = 0;
		for (final String entry[] : getEntryNamesAndValues()) {
			if (entry[0].equals(entry[0].toLowerCase())) {
				lowerCaseCount++;
			}
			else if (entry[0].equals(entry[0].toUpperCase())) {
				upperCaseCount++;
			}
		}
		if (upperCaseCount == getEntryNamesAndValues().length) {
			return LabelsCase.UPPER;
		}
		else if (lowerCaseCount == getEntryNamesAndValues().length) {
			return LabelsCase.LOWER;
		}
		else {
			return LabelsCase.MIXED;
		}
	}

	protected enum LabelsCase {
		UPPER,
		LOWER,
		MIXED;
	}

}
