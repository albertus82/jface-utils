package it.albertus.jface.validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Text;

public class DateTextValidator extends TextValidator {

	/**
	 * Use {@link #parseDate} and {@link #formatDate} synchronized methods
	 * instead.
	 */
	@Deprecated
	protected final DateFormat dateFormat;

	private final String pattern;
	private Date minValidValue;
	private Date maxValidValue;
	private boolean emptyStringAllowed = true; // Default

	public DateTextValidator(final Text text, final String pattern) {
		super(text);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
	}

	public DateTextValidator(final Text text, final String pattern, final Date minValidValue, final Date maxValidValue, final boolean emptyStringAllowed) {
		this(text, pattern);
		this.minValidValue = minValidValue;
		this.maxValidValue = maxValidValue;
		this.emptyStringAllowed = emptyStringAllowed;
	}

	@Override
	public boolean isValid() {
		final String value = text.getText();
		if (emptyStringAllowed && value.isEmpty()) {
			return true;
		}
		try {
			final Date date = parseDate(value);
			if (!((minValidValue != null && date.before(minValidValue)) || (maxValidValue != null && date.after(maxValidValue)))) {
				return true;
			}
		}
		catch (final Exception e) {/* Ignore */}
		return false;
	}

	protected synchronized Date parseDate(final String source) throws ParseException {
		return dateFormat.parse(source);
	}

	protected synchronized String formatDate(final Date date) {
		return dateFormat.format(date);
	}

	public String getPattern() {
		return pattern;
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

	public boolean isEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	public void setEmptyStringAllowed(final boolean emptyStringAllowed) {
		this.emptyStringAllowed = emptyStringAllowed;
	}

}
