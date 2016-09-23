package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;
import it.albertus.jface.listener.BigDecimalVerifyListener;
import it.albertus.util.Configured;

import java.math.BigDecimal;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultBigDecimalFieldEditor extends BigDecimalFieldEditor {

	public DefaultBigDecimalFieldEditor(final String name, final String labelText, final Composite parent, final int textLimit) {
		super(name, labelText, parent, textLimit);
		init();
	}

	public DefaultBigDecimalFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	@Override
	protected void doLoad() {
		final Text text = getTextControl();
		if (text != null && !text.isDisposed()) {
			try {
				setToolTipText(new BigDecimal(getPreferenceStore().getDefaultString(getPreferenceName())));
			}
			catch (final Exception e) {/* Ignore */}
			String value;
			try {
				value = new BigDecimal(getPreferenceStore().getString(getPreferenceName()).trim()).toString();
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
				if (text != null && "".equals(text.getText())) {
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
		getTextControl().addVerifyListener(new BigDecimalVerifyListener(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return getMinValidValue() == null || getMinValidValue().compareTo(BigDecimal.ZERO) < 0;
			}
		}));
		getTextControl().addFocusListener(new BigDecimalFocusListener());
		setErrorMessage(JFaceMessages.get("err.preferences.decimal"));
	}

	protected void setToolTipText(final BigDecimal defaultValue) {
		if (defaultValue != null) {
			getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected void updateFontStyle() {
		final String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		TextFormatter.updateFontStyle(getTextControl(), defaultValue);
	}

	/** Format the double when the field loses the focus */
	protected class BigDecimalFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			try {
				final String newText = new BigDecimal(oldText).toString();
				if (!oldText.equals(newText)) {
					text.setText(newText);
				}
				valueChanged();
			}
			catch (final Exception e) {}
		}
	}

}
