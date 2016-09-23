package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import java.math.BigDecimal;
import java.util.prefs.Preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class BigDecimalFieldEditor extends StringFieldEditor {

	private BigDecimal minValidValue;
	private BigDecimal maxValidValue;

	protected BigDecimalFieldEditor() {}

	public BigDecimalFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, parent, Preferences.MAX_VALUE_LENGTH);
	}

	public BigDecimalFieldEditor(String name, String labelText, Composite parent, int textLimit) {
		init(name, labelText);
		setTextLimit(textLimit);
		setEmptyStringAllowed(false);
		setErrorMessage(JFaceMessages.get("err.preferences.decimal"));
		createControl(parent);
	}

	public void setValidRange(Number min, Number max) {
		if (min instanceof BigDecimal) {
			minValidValue = (BigDecimal) min;
		}
		else {
			minValidValue = BigDecimal.valueOf(min.doubleValue());
		}
		if (max instanceof BigDecimal) {
			maxValidValue = (BigDecimal) max;
		}
		else {
			maxValidValue = BigDecimal.valueOf(max.doubleValue());
		}
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
			BigDecimal number = new BigDecimal(numberString);
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
				value = new BigDecimal(getPreferenceStore().getString(getPreferenceName())).toString();
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
				value = new BigDecimal(getPreferenceStore().getDefaultString(getPreferenceName())).toString();
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
			BigDecimal bd = new BigDecimal(text.getText());
			getPreferenceStore().setValue(getPreferenceName(), bd.toString());
		}
	}

	public BigDecimal getBigDecimalValue() throws NumberFormatException {
		return new BigDecimal(getStringValue());
	}

	public BigDecimal getMinValidValue() {
		return minValidValue;
	}

	public BigDecimal getMaxValidValue() {
		return maxValidValue;
	}

}
