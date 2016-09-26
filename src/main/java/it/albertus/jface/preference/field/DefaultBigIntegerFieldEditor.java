package it.albertus.jface.preference.field;

import it.albertus.jface.listener.BigIntegerVerifyListener;
import it.albertus.util.Configured;

import java.math.BigInteger;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultBigIntegerFieldEditor extends AbstractIntegerFieldEditor<BigInteger> {

	public DefaultBigIntegerFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		getTextControl().addVerifyListener(new BigIntegerVerifyListener(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return getMinValidValue() == null || getMinValidValue().intValue() < 0;
			}
		}));
		getTextControl().addFocusListener(new BigIntegerFocusListener());
	}

	@Override
	protected boolean doCheckState() {
		final Text text = getTextControl();
		try {
			final BigInteger number = new BigInteger(text.getText());
			if (checkValidRange(number)) {
				clearErrorMessage();
				return true;
			}
			showErrorMessage();
		}
		catch (final NumberFormatException nfe) {
			showErrorMessage();
		}
		return false;
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		final Text text = getTextControl();
		if (text != null) {
			String value;
			try {
				value = new BigInteger(getPreferenceStore().getString(getPreferenceName())).toString();
			}
			catch (final NumberFormatException nfe) {
				value = "";
			}
			text.setText(value);
			oldValue = value;
		}
		updateFontStyle();
	}

	@Override
	protected void doStore() throws NumberFormatException {
		final Text text = getTextControl();
		if (text != null) {
			if (text.getText().isEmpty() && isEmptyStringAllowed()) {
				getPreferenceStore().setValue(getPreferenceName(), "");
			}
			else {
				final BigInteger value = new BigInteger(text.getText());
				getPreferenceStore().setValue(getPreferenceName(), value.toString());
			}
		}
	}

	@Override
	protected String getDefaultValue() {
		final String defaultValue = super.getDefaultValue();
		try {
			new BigInteger(defaultValue);
			return defaultValue;
		}
		catch (final NumberFormatException nfe) {
			return "";
		}
	}

	@Override
	public BigInteger getNumberValue() throws NumberFormatException {
		return new BigInteger(getStringValue());
	}

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
