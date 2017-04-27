package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.listener.IntegerVerifyListener;
import it.albertus.util.Supplier;
import it.albertus.util.logging.LoggerFactory;

public class EnhancedIntegerFieldEditor extends AbstractIntegerFieldEditor<Integer> {

	private static final Logger logger = LoggerFactory.getLogger(EnhancedIntegerFieldEditor.class);

	private static final int DEFAULT_TEXT_LIMIT = Integer.toString(Integer.MAX_VALUE).length() - 1;

	public EnhancedIntegerFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		setMinValidValue(Integer.valueOf(0)); // Positive by default
		getTextControl().addVerifyListener(new IntegerVerifyListener(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return getMinValidValue() == null || getMinValidValue().intValue() < 0;
			}
		}));
		getTextControl().addFocusListener(new IntegerFocusListener());
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
				value = Integer.valueOf(getPreferenceStore().getString(getPreferenceName()).trim()).toString();
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
				final Integer value = Integer.valueOf(text.getText());
				getPreferenceStore().setValue(getPreferenceName(), value.toString());
			}
		}
	}

	@Override
	protected String getDefaultValue() {
		final String defaultValue = super.getDefaultValue();
		try {
			return Integer.valueOf(defaultValue).toString();
		}
		catch (final NumberFormatException nfe) {
			return "";
		}
	}

	@Override
	public Integer getNumberValue() throws NumberFormatException {
		return Integer.valueOf(getStringValue());
	}

	protected class IntegerFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			try {
				final String newText = Integer.toString(Integer.parseInt(oldText));
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
