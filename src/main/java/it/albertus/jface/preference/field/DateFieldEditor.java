package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DateFieldEditor extends StringFieldEditor {

	/**
	 * Use {@link #parseDate} and {@link #formatDate} synchronized methods
	 * instead.
	 */
	@Deprecated
	protected final DateFormat dateFormat;

	private final String pattern;

	private Date minValidValue;
	private Date maxValidValue;

	protected synchronized Date parseDate(final String source) throws ParseException {
		return dateFormat.parse(source);
	}

	protected synchronized String formatDate(final Date date) {
		return dateFormat.format(date);
	}

	public DateFieldEditor(final String name, final String labelText, final String pattern, final Composite parent) {
		super(name, labelText, parent);
		checkPattern(pattern);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	public DateFieldEditor(final String name, final String labelText, final String pattern, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		checkPattern(pattern);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	public DateFieldEditor(final String name, final String labelText, final String pattern, final int width, final int strategy, final Composite parent) {
		super(name, labelText, width, strategy, parent);
		checkPattern(pattern);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	@Override
	protected boolean doCheckState() {
		boolean result;
		Date date = null;
		if (isEmptyStringAllowed() && getTextControl().getText().isEmpty()) {
			result = true;
		}
		else {
			try {
				date = parseDate(getTextControl().getText());
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

	public void setValidRange(final Date from, final Date to) {
		minValidValue = from;
		maxValidValue = to;
		setErrorMessage(JFaceMessages.get("err.preferences.date.range", formatDate(from), formatDate(to)));
	}

	protected void checkPattern(final String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("Missing date pattern");
		}
	}

	protected void init() {
		dateFormat.setLenient(false);
		setErrorMessage(JFaceMessages.get("err.preferences.date", pattern));
		setTextLimit(Preferences.MAX_VALUE_LENGTH);
		final Text text = getTextControl();
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent fe) {
				try {
					text.setText(formatDate(parseDate(text.getText())));
				}
				catch (final ParseException pe) {/* Ignore */}
			}
		});
	}

	public String getPattern() {
		return pattern;
	}

	public Date getDateValue() throws ParseException {
		return parseDate(getTextControl().getText());
	}

	public Date getMinValidValue() {
		return minValidValue;
	}

	public void setMinValidValue(final Date minValidValue) {
		this.minValidValue = minValidValue;
	}

	public Date getMaxValidValue() {
		return maxValidValue;
	}

	public void setMaxValidValue(final Date maxValidValue) {
		this.maxValidValue = maxValidValue;
	}

}
