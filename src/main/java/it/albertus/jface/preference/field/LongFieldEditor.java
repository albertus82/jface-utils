package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.listener.LongVerifyListener;
import it.albertus.util.Supplier;
import it.albertus.util.logging.LoggerFactory;

public class LongFieldEditor extends AbstractIntegerFieldEditor<Long> {

	private static final Logger log = LoggerFactory.getLogger(LongFieldEditor.class);

	private static final int DEFAULT_TEXT_LIMIT = Long.toString(Long.MAX_VALUE).length() - 1;

	public LongFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		setMinValidValue(Long.valueOf(0L)); // Positive by default
		getTextControl().addVerifyListener(new LongVerifyListener(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
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
				value = Long.valueOf(getPreferenceStore().getString(getPreferenceName()).trim()).toString();
			}
			catch (final NumberFormatException e) {
				log.log(Level.FINEST, "The value provided does not contain a parsable long:", e);
				value = "";
			}
			text.setText(value);
			oldValue = value;
		}
		updateFontStyle();
	}

	@Override
	protected void doStore() {
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
			return Long.valueOf(defaultValue).toString();
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable long:", e);
			return "";
		}
	}

	@Override
	public Long getNumberValue() {
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
			catch (final Exception e) {
				log.log(Level.FINE, "Cannot change the value of the field:", e);
			}
		}
	}

}
