package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class LongFieldEditor extends StringFieldEditor {

	private static final int DEFAULT_TEXT_LIMIT = Long.toString(Long.MAX_VALUE).length();

	private long minValidValue = 0;
	private long maxValidValue = Long.MAX_VALUE;

	protected LongFieldEditor() {}

	public LongFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, parent, DEFAULT_TEXT_LIMIT);
	}

	public LongFieldEditor(String name, String labelText, Composite parent, int textLimit) {
		init(name, labelText);
		setTextLimit(textLimit);
		setEmptyStringAllowed(false);
		setErrorMessage(JFaceMessages.get("err.preferences.integer"));
		createControl(parent);
	}

	public void setValidRange(long min, long max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceMessages.get("err.preferences.integer.range", min, max));
	}

	@Override
	protected boolean checkState() {

		Text text = getTextControl();

		if (text == null) {
			return false;
		}

		String numberString = text.getText();
		try {
			long number = Long.parseLong(numberString);
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
			long value = getPreferenceStore().getLong(getPreferenceName());
			text.setText("" + value);
			oldValue = "" + value;
		}

	}

	@Override
	protected void doLoadDefault() {
		Text text = getTextControl();
		if (text != null) {
			long value = getPreferenceStore().getDefaultLong(getPreferenceName());
			text.setText("" + value);
		}
		valueChanged();
	}

	@Override
	protected void doStore() {
		Text text = getTextControl();
		if (text != null) {
			long f = Long.parseLong(text.getText());
			getPreferenceStore().setValue(getPreferenceName(), f);
		}
	}

	public long getLongValue() throws NumberFormatException {
		return Long.parseLong(getStringValue());
	}

	public long getMinValidValue() {
		return minValidValue;
	}

	public long getMaxValidValue() {
		return maxValidValue;
	}

}
