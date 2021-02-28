package it.albertus.jface.preference.field;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.JFaceMessages;

public class PasswordFieldEditor extends StringFieldEditor {

	private static final String DEFAULT_CHARSET_NAME = "UTF-8";

	private Text textField; // Do not set any value here!

	protected char[] oldValue;
	protected char[] value;

	private /* final */ MessageDigest messageDigest;
	private Charset charset;

	public PasswordFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	public PasswordFieldEditor(final String name, final String labelText, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		init();
	}

	public PasswordFieldEditor(final String name, final String labelText, final String hashAlgorithm, final Composite parent) throws NoSuchAlgorithmException {
		super(name, labelText, parent);
		if (hashAlgorithm != null && !hashAlgorithm.isEmpty()) {
			this.messageDigest = MessageDigest.getInstance(hashAlgorithm);
			this.charset = Charset.forName(DEFAULT_CHARSET_NAME);
		}
		init();
	}

	public PasswordFieldEditor(final String name, final String labelText, final String hashAlgorithm, final int width, final Composite parent) throws NoSuchAlgorithmException {
		super(name, labelText, width, parent);
		if (hashAlgorithm != null && !hashAlgorithm.isEmpty()) {
			this.messageDigest = MessageDigest.getInstance(hashAlgorithm);
			this.charset = Charset.forName(DEFAULT_CHARSET_NAME);
		}
		init();
	}

	@Override
	protected void doFillIntoGrid(final Composite parent, final int numColumns) {
		super.doFillIntoGrid(parent, numColumns);
		final Control control = getTextControl();
		if (control != null && !control.isDisposed() && control.getLayoutData() instanceof GridData) {
			final GridData gd = (GridData) control.getLayoutData();
			gd.widthHint = control.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			gd.grabExcessHorizontalSpace = false;
		}
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
			textField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(final FocusEvent fe) {
					if (messageDigest != null) {
						textField.setText("");
					}
				}

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
			value = getPreferenceStore().getString(getPreferenceName()).toCharArray();
			oldValue = value;
			if (messageDigest == null) {
				textField.setTextChars(value);
			}
			else {
				textField.setText("********");
			}
		}
	}

	@Override
	protected void doLoadDefault() { // Default password is never stored as hash
		if (textField != null) {
			textField.setTextChars(getPreferenceStore().getDefaultString(getPreferenceName()).toCharArray());
		}
		valueChanged();
	}

	@Override
	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), String.valueOf(value));
	}

	@Override
	protected synchronized void valueChanged() {
		if (messageDigest == null) {
			value = textField.getTextChars();
		}
		else {
			messageDigest.reset();
			value = DatatypeConverter.printHexBinary(messageDigest.digest(textField.getText().getBytes(charset))).toLowerCase(Locale.ROOT).toCharArray();
		}
		setPresentsDefaultValue(false);
		boolean oldState = isValid();
		refreshValidState();

		if (isValid() != oldState) {
			fireStateChanged(IS_VALID, oldState, isValid());
		}

		if (!Arrays.equals(value, oldValue)) {
			// Avoiding String.valueOf(...)
			fireValueChanged(VALUE, oldValue, value);
			oldValue = value;
		}
	}

	@Override
	protected boolean checkState() {
		boolean result;
		if (isEmptyStringAllowed()) {
			result = true;
		}
		else if (textField == null) {
			result = false;
		}
		else {
			char[] txt = textField.getTextChars();
			result = (trimCharArray(txt).length > 0) || isEmptyStringAllowed();
		}
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

	/**
	 * @deprecated Use the character-oriented method:
	 *             {@link #getCharArrayValue() getCharArrayValue}.
	 */
	@Override
	@Deprecated
	public String getStringValue() { // NOSONAR
		if (textField != null) {
			return String.valueOf(value);
		}
		return getPreferenceStore().getString(getPreferenceName());
	}

	/**
	 * @deprecated Use the character-oriented method:
	 *             {@link #setCharArrayValue() setCharArrayValue}.
	 */
	@Override
	@Deprecated
	public void setStringValue(final String value) { // NOSONAR
		setCharArrayValue(value.toCharArray());
	}

	public char[] getCharArrayValue() {
		if (textField != null) {
			return value;
		}
		return getPreferenceStore().getString(getPreferenceName()).toCharArray();
	}

	public void setCharArrayValue(final char[] value) {
		if (textField != null) {
			final char[] cleanValue;
			if (value != null) {
				cleanValue = value;
			}
			else {
				cleanValue = new char[0];
			}

			if (!Arrays.equals(textField.getTextChars(), cleanValue)) {
				textField.setTextChars(cleanValue);
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
		setErrorMessage(JFaceMessages.get("err.preferences.string"));
		setTextLimit(Preferences.MAX_VALUE_LENGTH);
	}

	protected Text getTextField() {
		return textField;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(final Charset charset) {
		this.charset = charset;
	}

}
