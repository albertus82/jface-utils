package io.github.albertus82.jface.preference.field;

import org.eclipse.swt.widgets.Composite;

import io.github.albertus82.jface.JFaceMessages;

abstract class AbstractDecimalFieldEditor<T extends Number & Comparable<? extends Number>> extends AbstractNumberFieldEditor<T> {

	protected AbstractDecimalFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
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
