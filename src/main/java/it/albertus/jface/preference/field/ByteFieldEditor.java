package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.listener.ByteVerifyListener;
import it.albertus.util.Supplier;
import it.albertus.util.logging.LoggerFactory;

public class ByteFieldEditor extends AbstractIntegerFieldEditor<Byte> {

	private static final Logger logger = LoggerFactory.getLogger(ByteFieldEditor.class);

	private static final int DEFAULT_TEXT_LIMIT = Byte.toString(Byte.MAX_VALUE).length();

	public ByteFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		setMinValidValue((byte) 0); // Positive by default
		setMaxValidValue(Byte.MAX_VALUE); // Not so ugly (127)
		getTextControl().addVerifyListener(new ByteVerifyListener(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return getMinValidValue() == null || getMinValidValue().byteValue() < 0;
			}
		}));
		getTextControl().addFocusListener(new ByteFocusListener());
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
				value = Byte.valueOf(getPreferenceStore().getString(getPreferenceName()).trim()).toString();
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
	protected void doStore() {
		final Text text = getTextControl();
		if (text != null) {
			if (text.getText().isEmpty() && isEmptyStringAllowed()) {
				getPreferenceStore().setValue(getPreferenceName(), "");
			}
			else {
				final Byte value = Byte.valueOf(text.getText());
				getPreferenceStore().setValue(getPreferenceName(), value.toString());
			}
		}
	}

	@Override
	protected String getDefaultValue() {
		final String defaultValue = super.getDefaultValue();
		try {
			return Byte.valueOf(defaultValue).toString();
		}
		catch (final NumberFormatException nfe) {
			return "";
		}
	}

	@Override
	public Byte getNumberValue() {
		return Byte.valueOf(getStringValue());
	}

	protected class ByteFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			try {
				final String newText = Byte.toString(Byte.parseByte(oldText));
				if (!oldText.equals(newText)) {
					text.setText(newText);
				}
				valueChanged();
			}
			catch (final Exception e) {
				logger.log(Level.FINE, e.toString(), e);
			}
		}
	}

}
