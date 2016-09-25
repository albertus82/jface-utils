package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.listener.LowerCaseVerifyListener;
import it.albertus.jface.listener.TrimVerifyListener;
import it.albertus.jface.listener.UpperCaseVerifyListener;

import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Composite;

public abstract class NumberComboFieldEditor extends ValidatedComboFieldEditor {

	protected final LabelsCase labelsCase;

	private Number minValidValue;
	private Number maxValidValue;

	public NumberComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
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

	protected boolean checkValidRange(Comparable number) {
		if ((getMinValidValue() == null || number.compareTo(getMinValidValue()) >= 0) && (getMaxValidValue() == null || number.compareTo(getMaxValidValue()) <= 0)) {
			return true;
		}
		return false;
	}

	public void setValidRange(final Number min, final Number max) {
		setMinValidValue(min);
		setMaxValidValue(max);
	}

	protected Number getMinValidValue() {
		return minValidValue;
	}

	protected void setMinValidValue(final Number min) {
		this.minValidValue = min;
		updateErrorMessage();
		updateTextLimit();
	}

	protected Number getMaxValidValue() {
		return maxValidValue;
	}

	protected void setMaxValidValue(final Number max) {
		this.maxValidValue = max;
		updateErrorMessage();
		updateTextLimit();
	}

	protected void updateTextLimit() {
		if (NumberType.INTEGER.equals(getNumberType())) {
			int maxNumberLength = getDefaultTextLimit();
			if (getMinValidValue() != null && getMaxValidValue() != null) {
				maxNumberLength = Math.max(getMinValidValue().toString().length(), getMaxValidValue().toString().length());
			}
			setTextLimit(Math.max(maxNumberLength, getMaxLabelLength()));
		}
	}

	protected boolean labelsContainWhitespace() {
		for (final String entry[] : getEntryNamesAndValues()) {
			if (entry[0].contains(" ")) {
				return true;
			}
		}
		return false;
	}

	protected abstract NumberType getNumberType();

	protected enum NumberType {
		INTEGER,
		DECIMAL;
	}

	protected void updateErrorMessage() {
		final String keyPart = getNumberType().name().toLowerCase();
		if (getMinValidValue() == null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences." + keyPart));
		}
		else if (getMinValidValue() != null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences." + keyPart + ".min", getMinValidValue()));
		}
		else if (getMinValidValue() == null && getMaxValidValue() != null) {
			setErrorMessage(JFaceMessages.get("err.preferences." + keyPart + ".max", getMaxValidValue()));
		}
		else {
			setErrorMessage(JFaceMessages.get("err.preferences." + keyPart + ".range", getMinValidValue(), getMaxValidValue()));
		}
	}

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
