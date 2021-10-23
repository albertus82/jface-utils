package io.github.albertus82.jface.preference.field;

import org.eclipse.swt.widgets.Composite;

import io.github.albertus82.jface.JFaceMessages;

abstract class AbstractIntegerFieldEditor<T extends Number & Comparable<? extends Number>> extends AbstractNumberFieldEditor<T> {

	protected AbstractIntegerFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected void updateTextLimit() {
		int maxNumberLength = getDefaultTextLimit();
		if (getMinValidValue() != null && getMaxValidValue() != null) {
			maxNumberLength = Math.max(getMinValidValue().toString().length(), getMaxValidValue().toString().length());
		}
		setTextLimit(maxNumberLength);
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
