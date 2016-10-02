package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;

import java.text.ParseException;
import java.util.Date;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DateFieldEditor extends AbstractDateFieldEditor implements FieldEditorDefault {

	private boolean defaultToolTip = true;
	private boolean boldCustomValues = true;

	public DateFieldEditor(final String name, final String labelText, final String pattern, final int style, final Composite parent) {
		super(name, labelText, pattern, style, parent);
	}

	public DateFieldEditor(final String name, final String labelText, final String pattern, final int style, final int width, final Composite parent) {
		super(name, labelText, pattern, style, width, parent);
	}

	public DateFieldEditor(final String name, final String labelText, final String pattern, final int style, final int width, final int strategy, final Composite parent) {
		super(name, labelText, pattern, style, width, strategy, parent);
	}

	@Override
	protected void init() {
		super.init();
		if (getTextControl() != null) {
			getTextControl().addFocusListener(new DateFocusListener());
		}
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText();
		updateFontStyle();
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
	}

	protected String getDefaultValue() {
		return getPreferenceStore().getDefaultString(getPreferenceName());
	}

	protected void setToolTipText() {
		if (defaultToolTip) {
			final String defaultValue = getDefaultValue();
			if (defaultValue != null && !defaultValue.isEmpty()) {
				if (getTextControl() != null && !getTextControl().isDisposed()) {
					getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
				}
				else if (getDateTimeControl() != null && !getDateTimeControl().isDisposed()) {
					getDateTimeControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
				}
			}
		}
	}

	protected void updateFontStyle() {
		if (boldCustomValues) {
			final String defaultValue = getDefaultValue();
			if (defaultValue != null && !defaultValue.isEmpty()) {
				if (getTextControl() != null) {
					TextFormatter.updateFontStyle(getTextControl(), defaultValue);
				}
				else if (getDateTimeControl() != null && !getDateTimeControl().isDisposed()) {
					try {
						final Date date = getDateValue();
						final String dateString = formatDate(date);
						if (defaultValue.equals(dateString)) {
							TextFormatter.setNormalFontStyle(getDateTimeControl());
						}
						else {
							TextFormatter.setBoldFontStyle(getDateTimeControl());
						}
					}
					catch (final ParseException pe) {
						pe.printStackTrace();
					}
				}
			}
		}
	}

	protected class DateFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			if (getValidateStrategy() == VALIDATE_ON_KEY_STROKE) {
				final Text text = (Text) fe.widget;
				final String oldText = text.getText();
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

	@Override
	public boolean isDefaultToolTip() {
		return defaultToolTip;
	}

	@Override
	public void setDefaultToolTip(final boolean defaultToolTip) {
		this.defaultToolTip = defaultToolTip;
	}

	@Override
	public boolean isBoldCustomValues() {
		return boldCustomValues;
	}

	@Override
	public void setBoldCustomValues(final boolean boldCustomValues) {
		this.boldCustomValues = boldCustomValues;
	}

}
