package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DoubleFieldEditor extends StringFieldEditor {

	private static final int DEFAULT_TEXT_LIMIT = 32;

	private double minValidValue = Double.NEGATIVE_INFINITY;
	private double maxValidValue = Double.POSITIVE_INFINITY;

	protected DoubleFieldEditor() {}

	public DoubleFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, parent, DEFAULT_TEXT_LIMIT);
	}

	public DoubleFieldEditor(String name, String labelText, Composite parent, int textLimit) {
		init(name, labelText);
		setTextLimit(textLimit);
		setEmptyStringAllowed(false);
		setErrorMessage(JFaceMessages.get("err.preferences.decimal"));
		createControl(parent);
	}

	public void setValidRange(double min, double max) {
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
			double number = Double.parseDouble(numberString);
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
			double value = getPreferenceStore().getDouble(getPreferenceName());
			text.setText("" + value);
			oldValue = "" + value;
		}

	}

	@Override
	protected void doLoadDefault() {
		Text text = getTextControl();
		if (text != null) {
			double value = getPreferenceStore().getDefaultDouble(getPreferenceName());
			text.setText("" + value);
		}
		valueChanged();
	}

	@Override
	protected void doStore() {
		Text text = getTextControl();
		if (text != null) {
			double d = Double.parseDouble(text.getText());
			getPreferenceStore().setValue(getPreferenceName(), d);
		}
	}

	public double getDoubleValue() throws NumberFormatException {
		return Double.parseDouble(getStringValue());
	}

	public double getMinValidValue() {
		return minValidValue;
	}

	public double getMaxValidValue() {
		return maxValidValue;
	}

}
