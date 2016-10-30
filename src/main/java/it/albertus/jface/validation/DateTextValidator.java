package it.albertus.jface.validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Text;

public class DateTextValidator extends StringTextValidator {

	public static final boolean EMPTY_STRING_ALLOWED = true;

	/**
	 * Use {@link #parseDate} and {@link #formatDate} synchronized methods
	 * instead.
	 */
	@Deprecated
	private final DateFormat dateFormat;

	private final String pattern;
	private Date minValidValue;
	private Date maxValidValue;

	public DateTextValidator(final Text text, final String pattern) {
		this(text, pattern, EMPTY_STRING_ALLOWED);
	}

	public DateTextValidator(final Text text, final String pattern, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
		this.dateFormat.setLenient(false);
	}

	public DateTextValidator(final Text text, final String pattern, final boolean emptyStringAllowed, final Date minValidValue, final Date maxValidValue) {
		this(text, pattern, emptyStringAllowed);
		this.minValidValue = minValidValue;
		this.maxValidValue = maxValidValue;
	}

	@Override
	public boolean isValid() {
		final String value = getControl().getText();
		if (isEmptyStringAllowed() && value.isEmpty()) {
			return true;
		}
		try {
			final Date date = parseDate(value);
			if (!((getMinValidValue() != null && date.before(getMinValidValue())) || (getMaxValidValue() != null && date.after(getMaxValidValue())))) {
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

}
