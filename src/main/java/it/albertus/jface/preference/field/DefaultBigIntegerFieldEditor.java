package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;
import it.albertus.jface.listener.BigIntegerVerifyListener;
import it.albertus.util.Configured;

import java.math.BigInteger;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultBigIntegerFieldEditor extends BigIntegerFieldEditor {

	public DefaultBigIntegerFieldEditor(final String name, final String labelText, final Composite parent, final int textLimit) {
		super(name, labelText, parent, textLimit);
		init();
	}

	public DefaultBigIntegerFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	@Override
	protected void doLoad() {
		final Text text = getTextControl();
		if (text != null && !text.isDisposed()) {
			try {
				setToolTipText(new BigInteger(getPreferenceStore().getDefaultString(getPreferenceName())));
			}
			catch (final Exception e) {/* Ignore */}
			String value;
			try {
				value = new BigInteger(getPreferenceStore().getString(getPreferenceName()).trim()).toString();
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
	protected void doStore() {
		if (!isEmptyStringAllowed()) {
			super.doStore();
		}
		else {
			final Text text = getTextControl();
			if (text != null) {
				getPreferenceStore().setValue(getPreferenceName(), text.getText());
			}
		}
	}

	@Override
	protected void doLoadDefault() {
		if (!isEmptyStringAllowed()) {
			super.doLoadDefault();
		}
		else {
			Text text = getTextControl();
			if (text != null) {
				text.setText(getPreferenceStore().getDefaultString(getPreferenceName()));
			}
			valueChanged();
		}
	}

	@Override
	protected boolean checkState() {
		if (!isEmptyStringAllowed()) {
			return super.checkState();
		}
		else {
			boolean state = super.checkState();
			if (!state) {
				final Text text = getTextControl();
				if (text != null && text.getText().isEmpty()) {
					clearErrorMessage();
					state = true;
				}
			}
			return state;
		}

	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
	}

	protected void init() {
		getTextControl().addVerifyListener(new BigIntegerVerifyListener(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return getMinValidValue() == null || getMinValidValue().compareTo(BigInteger.ZERO) < 0;
			}
		}));
		getTextControl().addFocusListener(new BigIntegerFocusListener());
		setErrorMessage(JFaceMessages.get("err.preferences.integer"));
	}

	protected void setToolTipText(final BigInteger defaultValue) {
		if (defaultValue != null) {
			getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected void updateFontStyle() {
		final String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		TextFormatter.updateFontStyle(getTextControl(), defaultValue);
	}

	/** Format the double when the field loses the focus */
	protected class BigIntegerFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			try {
				final String newText = new BigInteger(oldText).toString();
				if (!oldText.equals(newText)) {
					text.setText(newText);
				}
				valueChanged();
			}
			catch (final Exception e) {}
		}
	}

}
