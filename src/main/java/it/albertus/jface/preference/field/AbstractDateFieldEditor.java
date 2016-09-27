package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

abstract class AbstractDateFieldEditor extends StringFieldEditor {

	/**
	 * Use {@link #parseDate} and {@link #formatDate} synchronized methods
	 * instead.
	 */
	@Deprecated
	protected final DateFormat dateFormat;

	private final String pattern;

	private Date minValidValue;
	private Date maxValidValue;

	private int validateStrategy;

	protected synchronized Date parseDate(final String source) throws ParseException {
		return dateFormat.parse(source);
	}

	protected synchronized String formatDate(final Date date) {
		return dateFormat.format(date);
	}

	public AbstractDateFieldEditor(final String name, final String labelText, final String pattern, final Composite parent) {
		super(name, labelText, parent);
		this.validateStrategy = VALIDATE_ON_KEY_STROKE;
		checkPattern(pattern);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	public AbstractDateFieldEditor(final String name, final String labelText, final String pattern, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		this.validateStrategy = VALIDATE_ON_KEY_STROKE;
		checkPattern(pattern);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	public AbstractDateFieldEditor(final String name, final String labelText, final String pattern, final int width, final int strategy, final Composite parent) {
		super(name, labelText, width, strategy, parent);
		this.validateStrategy = strategy;
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
		if (date != null && ((getMinValidValue() != null && date.before(getMinValidValue())) || (getMaxValidValue() != null && date.after(getMaxValidValue())))) {
			result = false;
		}
		return result;
	}

	public void setValidRange(final Date from, final Date to) {
		setMinValidValue(from);
		setMaxValidValue(to);
	}

	protected void checkPattern(final String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("Missing date pattern");
		}
	}

	protected void init() {
		dateFormat.setLenient(false);
		updateErrorMessage();
		setTextLimit(Byte.MAX_VALUE);
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
		updateErrorMessage();
	}

	public Date getMaxValidValue() {
		return maxValidValue;
	}

	public void setMaxValidValue(final Date maxValidValue) {
		this.maxValidValue = maxValidValue;
		updateErrorMessage();
	}

	public int getValidateStrategy() {
		return validateStrategy;
	}

	@Override
	public void setValidateStrategy(final int value) {
		super.setValidateStrategy(value);
		this.validateStrategy = value;
	}

	protected void updateErrorMessage() {
		if (getMinValidValue() == null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences.date", pattern));
		}
		else if (getMinValidValue() != null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences.date.from", formatDate(getMinValidValue())));
		}
		else if (getMinValidValue() == null && getMaxValidValue() != null) {
			setErrorMessage(JFaceMessages.get("err.preferences.date.to", formatDate(getMaxValidValue())));
		}
		else {
			setErrorMessage(JFaceMessages.get("err.preferences.date.range", formatDate(getMinValidValue()), formatDate(getMaxValidValue())));
		}
	}

}
