package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.swt.widgets.Composite;

public abstract class AbstractIntegerFieldEditor<T extends Number & Comparable<? extends Number>> extends AbstractNumberFieldEditor<T> {

	public AbstractIntegerFieldEditor(String name, String labelText, Composite parent) {
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
