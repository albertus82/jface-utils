package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;
import it.albertus.jface.preference.field.listener.IntegerVerifyListener;
import it.albertus.util.Configured;

import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultIntegerFieldEditor extends IntegerFieldEditor {

	protected static final int DEFAULT_TEXT_LIMIT = Integer.toString(Integer.MAX_VALUE).length() - 1;

	private int minValidValue = 0;

	public DefaultIntegerFieldEditor(final String name, final String labelText, final Composite parent, final int textLimit) {
		super(name, labelText, parent, textLimit);
		init();
	}

	public DefaultIntegerFieldEditor(final String name, final String labelText, final Composite parent) {
		this(name, labelText, parent, DEFAULT_TEXT_LIMIT);
	}

	@Override
	public void setValidRange(final int min, final int max) {
		super.setValidRange(min, max);
		this.minValidValue = min;
		setErrorMessage(JFaceMessages.get("err.preferences.integer.range", min, max));
	}

	@Override
	protected void doLoad() {
		final Text text = getTextControl();
		if (text != null && !text.isDisposed()) {
			setToolTipText(getPreferenceStore().getDefaultInt(getPreferenceName()));
			String value;
			try {
				value = Integer.toString(Integer.parseInt(getPreferenceStore().getString(getPreferenceName()).trim()));
			}
			catch (final Exception e) {
				value = "";
			}
			text.setText(value);
			oldValue = value;
			updateFontStyle();
		}
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
	}

	protected void init() {
		getTextControl().addVerifyListener(new IntegerVerifyListener(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return minValidValue < 0;
			}
		}));
		getTextControl().addFocusListener(new IntegerFocusListener());
		setErrorMessage(JFaceMessages.get("err.preferences.integer"));
	}

	protected void setToolTipText(final int defaultValue) {
		if (defaultValue != 0) {
			getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected void updateFontStyle() {
		final int defaultValue = getPreferenceStore().getDefaultInt(getPreferenceName());
		TextFormatter.updateFontStyle(getTextControl(), defaultValue);
	}

	/** Removes trailing zeros when the field loses the focus */
	protected class IntegerFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			try {
				final String newText = Integer.toString(Integer.parseInt(oldText));
				if (!oldText.equals(newText)) {
					text.setText(newText);
				}
				valueChanged();
			}
			catch (final Exception e) {}
		}
	}

}
