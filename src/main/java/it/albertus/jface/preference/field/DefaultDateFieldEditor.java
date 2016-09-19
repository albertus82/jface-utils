package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class DefaultDateFieldEditor extends StringFieldEditor {

	protected final DateFormat dateFormat;
	private final String pattern;

	private Date minValidValue;
	private Date maxValidValue;

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final Composite parent) {
		super(name, labelText, parent);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final int width, final int strategy, final Composite parent) {
		super(name, labelText, width, strategy, parent);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
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

	@Override
	protected synchronized boolean doCheckState() {
		boolean result;
		Date date = null;
		if (isEmptyStringAllowed() && getTextControl().getText().isEmpty()) {
			result = true;
		}
		else {
			try {
				date = dateFormat.parse(getTextControl().getText());
				result = true;
			}
			catch (final ParseException pe) {
				result = false;
			}
		}
		if (minValidValue != null && maxValidValue != null && date != null && (date.before(minValidValue) || date.after(maxValidValue))) {
			result = false;
		}
		return result;
	}

	protected void setToolTipText(final String defaultValue) {
		if (getTextControl() != null && !getTextControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
			getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
		}
	}

	public void setValidRange(final Date min, final Date max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceMessages.get("err.preferences.date.range", dateFormat.format(min), dateFormat.format(max)));
	}

	protected void updateFontStyle() {
		final String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		if (defaultValue != null && !defaultValue.isEmpty()) {
			TextFormatter.updateFontStyle(getTextControl(), defaultValue);
		}
	}

	protected void init() {
		dateFormat.setLenient(false);
		setErrorMessage(JFaceMessages.get("err.preferences.date", pattern));
		setTextLimit(Preferences.MAX_VALUE_LENGTH);
	}

	public String getPattern() {
		return pattern;
	}

	public Date getDateValue() throws ParseException {
		return dateFormat.parse(getTextControl().getText());
	}
}
