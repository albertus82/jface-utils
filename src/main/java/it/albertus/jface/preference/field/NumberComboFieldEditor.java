package it.albertus.jface.preference.field;

import it.albertus.jface.listener.LowerCaseVerifyListener;
import it.albertus.jface.listener.TrimVerifyListener;
import it.albertus.jface.listener.UpperCaseVerifyListener;

import org.eclipse.swt.widgets.Composite;

public abstract class NumberComboFieldEditor extends ValidatedComboFieldEditor {

	protected final LabelsCase labelsCase;

	public NumberComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		labelsCase = getLabelsCase();

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
