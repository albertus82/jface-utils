package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public abstract class NumberTextValidator<T extends Number & Comparable<? extends Number>> extends TextValidator {

	private T minValidValue;
	private T maxValidValue;
	private boolean emptyStringAllowed = false; // Default

	public NumberTextValidator(final Text text) {
		super(text);
	}

	public NumberTextValidator(final Text text, final T minValidValue, final T maxValidValue, final boolean emptyStringAllowed) {
		this(text);
		this.minValidValue = minValidValue;
		this.maxValidValue = maxValidValue;
		this.emptyStringAllowed = emptyStringAllowed;
	}

	protected abstract Comparable<T> toNumber(String value) throws NumberFormatException;

	@Override
	public boolean isValid() {
		final String value = text.getText();
		if (emptyStringAllowed && value.isEmpty()) {
			return true;
		}
		try {
			final Comparable<T> number = toNumber(value);
			if ((minValidValue == null || number.compareTo(minValidValue) >= 0) && (maxValidValue == null || number.compareTo(maxValidValue) <= 0)) {
				return true;
			}
			else {
				throw new IllegalArgumentException(value);
			}
		}
		catch (final IllegalArgumentException nfe) {/* Ignore */}
		return false;
	}

	public T getMinValidValue() {
		return minValidValue;
	}

	public void setMinValidValue(T minValidValue) {
		this.minValidValue = minValidValue;
	}

	public T getMaxValidValue() {
		return maxValidValue;
	}

	public void setMaxValidValue(T maxValidValue) {
		this.maxValidValue = maxValidValue;
	}

	public boolean isEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	public void setEmptyStringAllowed(final boolean emptyStringAllowed) {
		this.emptyStringAllowed = emptyStringAllowed;
	}

}
