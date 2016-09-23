package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import java.math.BigInteger;
import java.util.prefs.Preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class BigIntegerFieldEditor extends StringFieldEditor {

	private BigInteger minValidValue;
	private BigInteger maxValidValue;

	protected BigIntegerFieldEditor() {}

	public BigIntegerFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, parent, Preferences.MAX_VALUE_LENGTH);
	}

	public BigIntegerFieldEditor(String name, String labelText, Composite parent, int textLimit) {
		init(name, labelText);
		setTextLimit(textLimit);
		setEmptyStringAllowed(false);
		setErrorMessage(JFaceMessages.get("err.preferences.integer"));
		createControl(parent);
	}

	public void setValidRange(Number min, Number max) {
		setMinValidValue(min);
		setMaxValidValue(max);
		setErrorMessage(JFaceMessages.get("err.preferences.integer.range", min, max));
	}

	protected void setMaxValidValue(Number max) {
		if (max instanceof BigInteger) {
			maxValidValue = (BigInteger) max;
		}
		else {
			maxValidValue = BigInteger.valueOf(max.longValue());
		}
	}

	protected void setMinValidValue(Number min) {
		if (min instanceof BigInteger) {
			minValidValue = (BigInteger) min;
		}
		else {
			minValidValue = BigInteger.valueOf(min.longValue());
		}
	}

	@Override
	protected boolean checkState() {

		Text text = getTextControl();

		if (text == null) {
			return false;
		}

		String numberString = text.getText();
		try {
			BigInteger number = new BigInteger(numberString);
			if (minValidValue == null || maxValidValue == null || (number.compareTo(minValidValue) >= 0 && number.compareTo(maxValidValue) <= 0)) {
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
			String value;
			try {
				value = new BigInteger(getPreferenceStore().getString(getPreferenceName())).toString();
			}
			catch (final Exception e) {
				value = "";
			}
			text.setText(value);
			oldValue = value;
		}

	}

	@Override
	protected void doLoadDefault() {
		Text text = getTextControl();
		if (text != null) {
			String value;
			try {
				value = new BigInteger(getPreferenceStore().getDefaultString(getPreferenceName())).toString();
			}
			catch (final Exception e) {
				value = "";
			}
			text.setText(value);
		}
		valueChanged();
	}

	@Override
	protected void doStore() {
		Text text = getTextControl();
		if (text != null) {
			BigInteger bi = new BigInteger(text.getText());
			getPreferenceStore().setValue(getPreferenceName(), bi.toString());
		}
	}

	public BigInteger getBigIntegerValue() throws NumberFormatException {
		return new BigInteger(getStringValue());
	}

	public BigInteger getMinValidValue() {
		return minValidValue;
	}

	public BigInteger getMaxValidValue() {
		return maxValidValue;
	}

}
