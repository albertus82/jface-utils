package it.albertus.jface.preference.field;

import it.albertus.jface.listener.LongVerifyListener;
import it.albertus.util.Configured;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultLongFieldEditor extends AbstractIntegerFieldEditor<Long> {

	private static final int DEFAULT_TEXT_LIMIT = Long.toString(Long.MAX_VALUE).length();

	public DefaultLongFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
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
	protected boolean doCheckState() {
		Text text = getTextControl();
		if (text == null) {
			return false;
		}
		String numberString = text.getText();
		try {
			final Long number = Long.valueOf(numberString);
			if (checkValidRange(number)) {
				clearErrorMessage();
				return true;
			}
			showErrorMessage();
			return false;
		}
		catch (final NumberFormatException nfe) {
			showErrorMessage();
		}
		return false;
	}

//	@Override
//	protected void doLoad() {
//		final Text text = getTextControl();
//		if (text != null && !text.isDisposed()) {
//			setToolTipText(getPreferenceStore().getDefaultString(getPreferenceName()));
//			String value;
//			try {
//				value = Long.toString(Long.parseLong(getPreferenceStore().getString(getPreferenceName()).trim()));
//			}
//			catch (final Exception e) {
//				value = "";
//			}
//			text.setText(value);
//			oldValue = value;
//			updateFontStyle();
//		}
//	}

//	@Override
//	protected void doStore() {
//		if (!isEmptyStringAllowed()) {
//			Text text = getTextControl();
//			if (text != null) {
//				long f = Long.parseLong(text.getText());
//				getPreferenceStore().setValue(getPreferenceName(), f);
//			}
//		}
//		else {
//			final Text text = getTextControl();
//			if (text != null) {
//				getPreferenceStore().setValue(getPreferenceName(), text.getText());
//			}
//		}
//	}

//	@Override
//	protected void doLoadDefault() {
//		if (!isEmptyStringAllowed()) {
//			Text text = getTextControl();
//			if (text != null) {
//				long value = getPreferenceStore().getDefaultLong(getPreferenceName());
//				text.setText("" + value);
//			}
//		}
//		else {
//			Text text = getTextControl();
//			if (text != null) {
//				text.setText(getPreferenceStore().getDefaultString(getPreferenceName()));
//			}
//		}
//		valueChanged();
//	}

	/** Removes trailing zeros when the field loses the focus */
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

	@Override
	public Long getNumberValue() throws NumberFormatException {
		return Long.valueOf(getStringValue());
	}

}
