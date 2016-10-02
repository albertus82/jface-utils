package it.albertus.jface.preference.field;

import it.albertus.jface.listener.LongVerifyListener;
import it.albertus.util.Configured;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class LongFieldEditor extends AbstractIntegerFieldEditor<Long> {

	private static final int DEFAULT_TEXT_LIMIT = Long.toString(Long.MIN_VALUE).length();

	public LongFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		setMinValidValue(Long.valueOf(0L)); // Positive by default
		getTextControl().addVerifyListener(new LongVerifyListener(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return getMinValidValue() == null || getMinValidValue().longValue() < 0;
			}
		}));
		getTextControl().addFocusListener(new LongFocusListener());
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
				value = Long.valueOf(getPreferenceStore().getString(getPreferenceName())).toString();
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
				final Long value = Long.valueOf(text.getText());
				getPreferenceStore().setValue(getPreferenceName(), value.toString());
			}
		}
	}

	@Override
	protected String getDefaultValue() {
		final String defaultValue = super.getDefaultValue();
		try {
			Long.parseLong(defaultValue);
			return defaultValue;
		}
		catch (final NumberFormatException nfe) {
			return "";
		}
	}

	@Override
	public Long getNumberValue() throws NumberFormatException {
		return Long.valueOf(getStringValue());
	}

	protected class LongFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			try {
				final String newText = Long.toString(Long.parseLong(oldText));
				if (!oldText.equals(newText)) {
					text.setText(newText);
				}
				valueChanged();
			}
			catch (final Exception e) {}
		}
	}

}
