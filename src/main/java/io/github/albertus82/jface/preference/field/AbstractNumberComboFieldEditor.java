package io.github.albertus82.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Composite;

import io.github.albertus82.jface.listener.LowerCaseVerifyListener;
import io.github.albertus82.jface.listener.TrimVerifyListener;
import io.github.albertus82.jface.listener.UpperCaseVerifyListener;
import io.github.albertus82.util.logging.LoggerFactory;

abstract class AbstractNumberComboFieldEditor<T extends Number & Comparable<? extends Number>> extends ValidatedComboFieldEditor {

	private static final Logger log = LoggerFactory.getLogger(AbstractNumberComboFieldEditor.class);

	protected final LabelsCase labelsCase;

	private T minValidValue;
	private T maxValidValue;

	protected AbstractNumberComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setEmptyStringAllowed(false);
		labelsCase = getLabelsCase();

		// If all the labels are upper or lower case, force the input case.
		if (LabelsCase.UPPER.equals(labelsCase)) {
			getComboBoxControl().addVerifyListener(new UpperCaseVerifyListener());
		}
		else if (LabelsCase.LOWER.equals(labelsCase)) {
			getComboBoxControl().addVerifyListener(new LowerCaseVerifyListener());
		}

		// If none of the labels contain whitespace, disable the space bar.
		if (!labelsContainWhitespace()) {
			getComboBoxControl().addVerifyListener(new TrimVerifyListener());
		}

		// Compute text limit...
		setTextLimit(Math.max(getMaxLabelLength(), getDefaultTextLimit()));

		updateErrorMessage();
	}

	@Override
	protected String getDefaultValue() {
		return cleanValue(super.getDefaultValue());
	}

	@Override
	protected boolean doCheckState() {
		try {
			return checkValidRange(getNumberValue());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided is not a valid representation of a number:", e);
		}
		return false;
	}

	protected int getDefaultTextLimit() {
		return Preferences.MAX_VALUE_LENGTH;
	}

	protected boolean checkValidRange(final Comparable<T> number) {
		return (getMinValidValue() == null || number.compareTo(getMinValidValue()) >= 0) && (getMaxValidValue() == null || number.compareTo(getMaxValidValue()) <= 0);
	}

	public void setValidRange(final T min, final T max) {
		setMinValidValue(min);
		setMaxValidValue(max);
	}

	public T getMinValidValue() {
		return minValidValue;
	}

	public void setMinValidValue(final T min) {
		this.minValidValue = min;
		updateErrorMessage();
		updateTextLimit();
	}

	public T getMaxValidValue() {
		return maxValidValue;
	}

	public void setMaxValidValue(final T max) {
		this.maxValidValue = max;
		updateErrorMessage();
		updateTextLimit();
	}

	protected boolean labelsContainWhitespace() {
		for (final String[] entry : getEntryNamesAndValues()) {
			if (entry[0].contains(" ")) {
				return true;
			}
		}
		return false;
	}

	protected enum LabelsCase {
		UPPER,
		LOWER,
		MIXED;
	}

	protected LabelsCase getLabelsCase() {
		int upperCaseCount = 0;
		int lowerCaseCount = 0;
		for (final String[] entry : getEntryNamesAndValues()) {
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

	public abstract Comparable<T> getNumberValue();

	protected abstract void updateTextLimit();

	protected abstract void updateErrorMessage();

}
