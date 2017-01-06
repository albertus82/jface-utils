package it.albertus.jface.preference.field;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.Formatter;
import it.albertus.jface.JFaceMessages;
import it.albertus.util.logging.LoggerFactory;

public class DateFieldEditor extends AbstractDateFieldEditor implements FieldEditorDefault {

	private static final Logger logger = LoggerFactory.getLogger(DateFieldEditor.class);

	private static final Formatter formatter = new Formatter(DateFieldEditor.class);

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
	protected void init(final String pattern, final int style, final int validateStrategy, final Composite parent) {
		super.init(pattern, style, validateStrategy, parent);
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
					formatter.updateFontStyle(getTextControl(), defaultValue);
				}
				else if (getDateTimeControl() != null) {
					try {
						final Date date = getDateValue();
						formatter.updateFontStyle(getDateTimeControl(), defaultValue, dateFormat.get().format(date));
					}
					catch (final ParseException pe) {
						logger.log(Level.SEVERE, JFaceMessages.get("err.date.parse"), pe);
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
					final DateFormat df = dateFormat.get();
					final String newText = df.format(df.parse(getTextControl().getText()));
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
