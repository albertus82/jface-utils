package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceResources;

import java.util.Arrays;
import java.util.prefs.Preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class PasswordFieldEditor extends StringFieldEditor {

	private Text textField; // Do not set any value here!

	protected char[] oldValue;

	public PasswordFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	public PasswordFieldEditor(final String name, final String labelText, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		init();
	}

	@Override
	public Text getTextControl(final Composite parent) {
		if (textField == null) {
			textField = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
			textField.setFont(parent.getFont());
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(final KeyEvent ke) {
					valueChanged();
				}
			});
			textField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent fe) {
					valueChanged();
				}
			});
			textField.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(final DisposeEvent de) {
					textField = null;
				}
			});
		}
		else {
			checkParent(textField, parent);
		}
		return textField;
	}

	@Override
	protected void doLoad() {
		if (textField != null) {
			final char[] value = getPreferenceStore().getString(getPreferenceName()).toCharArray();
			textField.setTextChars(value);
			oldValue = value;
		}
	}

	@Override
	protected void doLoadDefault() {
		if (textField != null) {
			textField.setTextChars(getPreferenceStore().getDefaultString(getPreferenceName()).toCharArray());
		}
		valueChanged();
	}

	@Override
	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), String.valueOf(textField.getTextChars()));
	}

	@Override
	protected void valueChanged() {
		setPresentsDefaultValue(false);
		boolean oldState = isValid();
		refreshValidState();

		if (isValid() != oldState) {
			fireStateChanged(IS_VALID, oldState, isValid());
		}

		char[] newValue = textField.getTextChars();
		if (!newValue.equals(oldValue)) {
			// Avoiding String.valueOf(...)
			fireValueChanged(VALUE, oldValue, newValue);
			oldValue = newValue;
		}
	}

	@Override
	protected boolean checkState() {
		boolean result = false;
		if (isEmptyStringAllowed()) {
			result = true;
		}

		if (textField == null) {
			result = false;
		}

		char[] txt = textField.getTextChars();

		result = (trimCharArray(txt).length > 0) || isEmptyStringAllowed();

		// call hook for subclasses
		result = result && doCheckState();

		if (result) {
			clearErrorMessage();
		}
		else {
			showErrorMessage(getErrorMessage());
		}

		return result;
	}

	@Override
	@Deprecated
	public String getStringValue() {
		if (textField != null) {
			return String.valueOf(textField.getTextChars());
		}
		return getPreferenceStore().getString(getPreferenceName());
	}

	@Override
	@Deprecated
	public void setStringValue(String value) {
		setCharArrayValue(value.toCharArray());
	}

	public char[] getCharArrayValue() {
		if (textField != null) {
			return textField.getTextChars();
		}
		return getPreferenceStore().getString(getPreferenceName()).toCharArray();
	}

	public void setCharArrayValue(char[] value) {
		if (textField != null) {
			if (value == null) {
				value = new char[0];
			}
			oldValue = textField.getTextChars();
			if (!Arrays.equals(oldValue, value)) {
				textField.setTextChars(value);
				valueChanged();
			}
		}
	}

	protected char[] trimCharArray(final char[] txt) {
		int st = 0;
		int len = txt.length;
		while ((st < len) && (txt[st] <= ' ')) {
			st++;
		}
		while ((st < len) && (txt[len - 1] <= ' ')) {
			len--;
		}
		return ((st > 0) || (len < txt.length)) ? Arrays.copyOfRange(txt, st, len) : txt;
	}

	protected void init() {
		setErrorMessage(JFaceResources.get("err.preferences.string"));
		setTextLimit(Preferences.MAX_VALUE_LENGTH);
	}

	protected Text getTextField() {
		return textField;
	}

}
