package it.albertus.jface.preference.field;

import it.albertus.jface.listener.ShortVerifyListener;
import it.albertus.util.Configured;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ShortFieldEditor extends AbstractIntegerFieldEditor<Short> {

	private static final int DEFAULT_TEXT_LIMIT = Short.toString(Short.MIN_VALUE).length();

	public ShortFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		setMinValidValue((short) 0); // Positive by default
		getTextControl().addVerifyListener(new ShortVerifyListener(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return getMinValidValue() == null || getMinValidValue().shortValue() < 0;
			}
		}));
		getTextControl().addFocusListener(new ShortFocusListener());
	}

	@Override
	protected int getDefaultTextLimit() {
		return DEFAULT_TEXT_LIMIT;
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		final Text text = getTextControl();
		if (text != null) {
			String value;
			try {
				value = Short.valueOf(getPreferenceStore().getString(getPreferenceName())).toString();
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
				final Short value = Short.valueOf(text.getText());
				getPreferenceStore().setValue(getPreferenceName(), value.toString());
			}
		}
	}

	@Override
	protected String getDefaultValue() {
		final String defaultValue = super.getDefaultValue();
		try {
			Short.parseShort(defaultValue);
			return defaultValue;
		}
		catch (final NumberFormatException nfe) {
			return "";
		}
	}

	@Override
	public Short getNumberValue() throws NumberFormatException {
		return Short.valueOf(getStringValue());
	}

	protected class ShortFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			try {
				final String newText = Short.toString(Short.parseShort(oldText));
				if (!oldText.equals(newText)) {
					text.setText(newText);
				}
				valueChanged();
			}
			catch (final Exception e) {}
		}
	}

}
