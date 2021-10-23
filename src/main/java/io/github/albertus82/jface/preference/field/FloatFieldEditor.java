package io.github.albertus82.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import io.github.albertus82.jface.listener.FloatVerifyListener;
import io.github.albertus82.util.Supplier;
import io.github.albertus82.util.logging.LoggerFactory;

public class FloatFieldEditor extends AbstractDecimalFieldEditor<Float> {

	private static final Logger log = LoggerFactory.getLogger(FloatFieldEditor.class);

	private static final int DEFAULT_TEXT_LIMIT = 16;

	public FloatFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		getTextControl().addVerifyListener(new FloatVerifyListener(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return getMinValidValue() == null || getMinValidValue().floatValue() < 0;
			}
		}));
		getTextControl().addFocusListener(new FloatFocusListener());
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
				value = Float.valueOf(getPreferenceStore().getString(getPreferenceName()).trim()).toString();
			}
			catch (final NumberFormatException e) {
				log.log(Level.FINEST, "The value provided does not contain a parsable float:", e);
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
				final Float value = Float.valueOf(text.getText());
				getPreferenceStore().setValue(getPreferenceName(), value.toString());
			}
		}
	}

	@Override
	protected String getDefaultValue() {
		final String defaultValue = super.getDefaultValue();
		try {
			return Float.valueOf(defaultValue).toString();
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided does not contain a parsable float:", e);
			return "";
		}
	}

	@Override
	public Float getNumberValue() {
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
			catch (final Exception e) {
				log.log(Level.FINE, "Cannot change the value of the field:", e);
			}
		}
	}

}
