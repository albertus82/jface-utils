package it.albertus.jface.validation;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Text;

import it.albertus.util.logging.LoggerFactory;

public abstract class NumberTextValidator<T extends Number & Comparable<? extends Number>> extends StringTextValidator {

	private static final Logger logger = LoggerFactory.getLogger(NumberTextValidator.class);

	private static final boolean EMPTY_STRING_ALLOWED = false;

	private T minValidValue;
	private T maxValidValue;

	public NumberTextValidator(final Text text) {
		super(text, EMPTY_STRING_ALLOWED);
	}

	public NumberTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
	}

	public NumberTextValidator(final Text text, final boolean emptyStringAllowed, final T minValidValue, final T maxValidValue) {
		this(text, emptyStringAllowed);
		this.minValidValue = minValidValue;
		this.maxValidValue = maxValidValue;
	}

	protected abstract Comparable<T> toNumber(String value);

	@Override
	public boolean isValid() {
		final String value = getControl().getText();
		if (isEmptyStringAllowed() && value.isEmpty()) {
			return true;
		}
		try {
			final Comparable<T> number = toNumber(value);
			if ((getMinValidValue() == null || number.compareTo(getMinValidValue()) >= 0) && (getMaxValidValue() == null || number.compareTo(getMaxValidValue()) <= 0)) {
				return true;
			}
		}
		catch (final RuntimeException e) {
			logger.log(Level.FINE, e.toString(), e);
		}
		return false;
	}

	public T getMinValidValue() {
		return minValidValue;
	}

	public void setMinValidValue(final T minValidValue) {
		this.minValidValue = minValidValue;
	}

	public T getMaxValidValue() {
		return maxValidValue;
	}

	public void setMaxValidValue(final T maxValidValue) {
		this.maxValidValue = maxValidValue;
	}

}
