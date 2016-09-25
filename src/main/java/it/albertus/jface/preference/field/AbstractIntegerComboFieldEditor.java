package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.swt.widgets.Composite;

public abstract class AbstractIntegerComboFieldEditor<T extends Number & Comparable<? extends Number>> extends AbstractNumberComboFieldEditor<T> {

	public AbstractIntegerComboFieldEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected void updateTextLimit() {
		int maxNumberLength = getDefaultTextLimit();
		if (getMinValidValue() != null && getMaxValidValue() != null) {
			maxNumberLength = Math.max(getMinValidValue().toString().length(), getMaxValidValue().toString().length());
		}
		setTextLimit(Math.max(maxNumberLength, getMaxLabelLength()));
	}

	@Override
	protected void updateErrorMessage() {
		if (getMinValidValue() == null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences.integer"));
		}
		else if (getMinValidValue() != null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences.integer.min", getMinValidValue()));
		}
		else if (getMinValidValue() == null && getMaxValidValue() != null) {
			setErrorMessage(JFaceMessages.get("err.preferences.integer.max", getMaxValidValue()));
		}
		else {
			setErrorMessage(JFaceMessages.get("err.preferences.integer.range", getMinValidValue(), getMaxValidValue()));
		}
	}

}
