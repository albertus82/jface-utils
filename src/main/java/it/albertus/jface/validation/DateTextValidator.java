package it.albertus.jface.validation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Text;

public class DateTextValidator extends StringTextValidator {

	public static final boolean EMPTY_STRING_ALLOWED = true;

	protected final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			final DateFormat dateFormat = new SimpleDateFormat(pattern);
			dateFormat.setLenient(false);
			return dateFormat;
		}
	};

	private final String pattern;
	private Date minValidValue;
	private Date maxValidValue;

	public DateTextValidator(final Text text, final String pattern) {
		this(text, pattern, EMPTY_STRING_ALLOWED);
	}

	public DateTextValidator(final Text text, final String pattern, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
		this.pattern = pattern;
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
			final Date date = dateFormat.get().parse(value);
			if (!((getMinValidValue() != null && date.before(getMinValidValue())) || (getMaxValidValue() != null && date.after(getMaxValidValue())))) {
				return true;
			}
		}
		catch (final Exception e) {/* Ignore */}
		return false;
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
