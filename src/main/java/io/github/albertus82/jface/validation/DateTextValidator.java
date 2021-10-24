package io.github.albertus82.jface.validation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Text;

import io.github.albertus82.util.logging.LoggerFactory;

public class DateTextValidator extends StringTextValidator {

	private static final Logger log = LoggerFactory.getLogger(DateTextValidator.class);

	private static final boolean EMPTY_STRING_ALLOWED = true;

	protected final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			final DateFormat df = new SimpleDateFormat(pattern);
			df.setLenient(false);
			return df;
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
		catch (final Exception e) {
			log.log(Level.FINE, "An error occurred while validating the date:", e);
		}
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
