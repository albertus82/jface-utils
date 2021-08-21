package it.albertus.jface.preference.field;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.listener.BigIntegerVerifyListener;
import it.albertus.util.Supplier;
import it.albertus.util.logging.LoggerFactory;

public class BigIntegerFieldEditor extends AbstractIntegerFieldEditor<BigInteger> {

	private static final Logger log = LoggerFactory.getLogger(BigIntegerFieldEditor.class);

	public BigIntegerFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		getTextControl().addVerifyListener(new BigIntegerVerifyListener(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return getMinValidValue() == null || getMinValidValue().intValue() < 0;
			}
		}));
		getTextControl().addFocusListener(new BigIntegerFocusListener());
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		final Text text = getTextControl();
		if (text != null) {
			String value;
			try {
				value = new BigInteger(getPreferenceStore().getString(getPreferenceName()).trim()).toString();
			}
			catch (final NumberFormatException e) {
				log.log(Level.FINEST, "The value provided is not a valid representation of a BigInteger:", e);
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
				final BigInteger value = new BigInteger(text.getText());
				getPreferenceStore().setValue(getPreferenceName(), value.toString());
			}
		}
	}

	@Override
	protected String getDefaultValue() {
		final String defaultValue = super.getDefaultValue();
		try {
			return new BigInteger(defaultValue).toString();
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided is not a valid representation of a BigInteger:", e);
			return "";
		}
	}

	@Override
	public BigInteger getNumberValue() {
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
			catch (final Exception e) {
				log.log(Level.FINE, "Cannot change the value of the field:", e);
			}
		}
	}

}
