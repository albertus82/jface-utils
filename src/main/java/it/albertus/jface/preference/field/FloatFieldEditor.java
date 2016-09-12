package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class FloatFieldEditor extends StringFieldEditor {

	private static final int DEFAULT_TEXT_LIMIT = 16;

	private float minValidValue = Float.NEGATIVE_INFINITY;
	private float maxValidValue = Float.POSITIVE_INFINITY;

	protected FloatFieldEditor() {}

	public FloatFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, parent, DEFAULT_TEXT_LIMIT);
	}

	public FloatFieldEditor(String name, String labelText, Composite parent, int textLimit) {
		init(name, labelText);
		setTextLimit(textLimit);
		setEmptyStringAllowed(false);
		setErrorMessage(JFaceMessages.get("err.preferences.decimal"));
		createControl(parent);
	}

	public void setValidRange(float min, float max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceMessages.get("err.preferences.decimal.range", min, max));
	}

	@Override
	protected boolean checkState() {

		Text text = getTextControl();

		if (text == null) {
			return false;
		}

		String numberString = text.getText();
		try {
			float number = Float.parseFloat(numberString);
			if (number >= minValidValue && number <= maxValidValue) {
				clearErrorMessage();
				return true;
			}

			showErrorMessage();
			return false;

		}
		catch (NumberFormatException e1) {
			showErrorMessage();
		}

		return false;
	}

	@Override
	protected void doLoad() {
		Text text = getTextControl();
		if (text != null) {
			float value = getPreferenceStore().getFloat(getPreferenceName());
			text.setText("" + value);
			oldValue = "" + value;
		}

	}

	@Override
	protected void doLoadDefault() {
		Text text = getTextControl();
		if (text != null) {
			float value = getPreferenceStore().getDefaultFloat(getPreferenceName());
			text.setText("" + value);
		}
		valueChanged();
	}

	@Override
	protected void doStore() {
		Text text = getTextControl();
		if (text != null) {
			float f = Float.parseFloat(text.getText());
			getPreferenceStore().setValue(getPreferenceName(), f);
		}
	}

	public float getFloatValue() throws NumberFormatException {
		return Float.parseFloat(getStringValue());
	}

}
