package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;

import java.text.ParseException;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultDateFieldEditor extends DateFieldEditor {

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final Composite parent) {
		super(name, labelText, pattern, parent);
	}

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final int width, final Composite parent) {
		super(name, labelText, pattern, width, parent);
	}

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final int width, final int strategy, final Composite parent) {
		super(name, labelText, pattern, width, strategy, parent);
	}

	@Override
	protected void init() {
		super.init();
		getTextControl().addFocusListener(new DateFocusListener());
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText(getPreferenceStore().getDefaultString(getPreferenceName()));
		updateFontStyle();
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
	}

	protected void setToolTipText(final String defaultValue) {
		if (getTextControl() != null && !getTextControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
			getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected void updateFontStyle() {
		final String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		if (defaultValue != null && !defaultValue.isEmpty()) {
			TextFormatter.updateFontStyle(getTextControl(), defaultValue);
		}
	}

	protected class DateFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			final Text text = (Text) fe.widget;
			final String oldText = text.getText();
			if (getValidateStrategy() == VALIDATE_ON_KEY_STROKE) {
				try {
					final String newText = formatDate(parseDate(getTextControl().getText()));
					if (!oldText.equals(newText)) {
						text.setText(newText);
					}
					valueChanged();
				}
				catch (final ParseException pe) {/* Ignore */}
			}
		}
	}

}
