package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import java.util.prefs.Preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class WrapStringFieldEditor extends StringFieldEditor {

	public static final int DEFAULT_TEXT_HEIGHT = 4;

	private Text textField; // Do not set any value here!

	public WrapStringFieldEditor(final String name, final String labelText, final Composite parent) {
		this(name, labelText, parent, DEFAULT_TEXT_HEIGHT);
	}

	public WrapStringFieldEditor(final String name, final String labelText, final Composite parent, final int height) {
		super(name, labelText, parent);
		adjustTextHeight(height);
		setErrorMessage(JFaceMessages.get("err.preferences.string"));
		setTextLimit(Preferences.MAX_VALUE_LENGTH);
	}

	@Override
	public Text getTextControl(final Composite parent) {
		if (textField == null) {
			textField = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
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

	protected void adjustTextHeight(final int height) {
		final GridData gd = (GridData) getTextControl().getLayoutData();
		gd.heightHint = getTextControl().getLineHeight() * height;
		gd.widthHint = 0;
	}

	protected Text getTextField() {
		return textField;
	}

}
