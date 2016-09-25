package it.albertus.jface.preference.field;

import it.albertus.jface.listener.LowerCaseVerifyListener;
import it.albertus.jface.listener.TrimVerifyListener;
import it.albertus.jface.listener.UpperCaseVerifyListener;

import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Composite;

public abstract class AbstractNumberComboFieldEditor<T extends Number & Comparable<? extends Number>> extends ValidatedComboFieldEditor {

	protected final LabelsCase labelsCase;

	private T minValidValue;
	private T maxValidValue;

	public AbstractNumberComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
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

	protected int getDefaultTextLimit() {
		return Preferences.MAX_VALUE_LENGTH;
	}

	protected boolean checkValidRange(Comparable<T> number) {
		if ((getMinValidValue() == null || number.compareTo(getMinValidValue()) >= 0) && (getMaxValidValue() == null || number.compareTo(getMaxValidValue()) <= 0)) {
			return true;
		}
		return false;
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

	public abstract T getNumberValue() throws NumberFormatException;

	protected abstract void updateTextLimit();

	protected boolean labelsContainWhitespace() {
		for (final String entry[] : getEntryNamesAndValues()) {
			if (entry[0].contains(" ")) {
				return true;
			}
		}
		return false;
	}

	protected abstract void updateErrorMessage();

	protected enum LabelsCase {
		UPPER,
		LOWER,
		MIXED;
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

}
