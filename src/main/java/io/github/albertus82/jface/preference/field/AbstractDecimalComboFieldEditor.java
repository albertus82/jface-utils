package io.github.albertus82.jface.preference.field;

import org.eclipse.swt.widgets.Composite;

import io.github.albertus82.jface.JFaceMessages;

abstract class AbstractDecimalComboFieldEditor<T extends Number & Comparable<? extends Number>> extends AbstractNumberComboFieldEditor<T> {

	protected AbstractDecimalComboFieldEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected void updateTextLimit() {}

	@Override
	protected void updateErrorMessage() {
		if (getMinValidValue() == null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences.decimal"));
		}
		else if (getMinValidValue() != null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences.decimal.min", getMinValidValue()));
		}
		else if (getMinValidValue() == null && getMaxValidValue() != null) {
			setErrorMessage(JFaceMessages.get("err.preferences.decimal.max", getMaxValidValue()));
		}
		else {
			setErrorMessage(JFaceMessages.get("err.preferences.decimal.range", getMinValidValue(), getMaxValidValue()));
		}
	}

}
