package it.albertus.jface.preference.field;

import it.albertus.jface.listener.FloatVerifyListener;
import it.albertus.util.Configured;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultFloatFieldEditor extends AbstractDecimalFieldEditor<Float> {

	public DefaultFloatFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		getTextControl().addVerifyListener(new FloatVerifyListener(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return getMinValidValue() == null || getMinValidValue().floatValue() < 0;
			}
		}));
		getTextControl().addFocusListener(new FloatFocusListener());
	}

	@Override
	protected boolean doCheckState() {
		final Text text = getTextControl();
		try {
			final Float number = Float.valueOf(text.getText());
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
				value = Float.valueOf(getPreferenceStore().getString(getPreferenceName())).toString();
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
				final Float value = Float.valueOf(text.getText());
				getPreferenceStore().setValue(getPreferenceName(), value.toString());
			}
		}
	}

	@Override
	protected String getDefaultValue() {
		final String defaultValue = super.getDefaultValue();
		try {
			Float.parseFloat(defaultValue);
			return defaultValue;
		}
		catch (final NumberFormatException nfe) {
			return "";
		}
	}

	@Override
	public Float getNumberValue() throws NumberFormatException {
		return Float.valueOf(getStringValue());
	}

	protected class FloatFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			try {
				final String newText = Float.toString(Float.parseFloat(oldText));
				if (!oldText.equals(newText)) {
					text.setText(newText);
				}
				valueChanged();
			}
			catch (final Exception e) {}
		}
	}

}
