package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.swt.widgets.Composite;

abstract class AbstractDecimalFieldEditor<T extends Number & Comparable<? extends Number>> extends AbstractNumberFieldEditor<T> {

	public AbstractDecimalFieldEditor(String name, String labelText, Composite parent) {
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
