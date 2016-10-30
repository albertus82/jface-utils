package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public abstract class NumberTextValidator<T extends Number & Comparable<? extends Number>> extends ControlValidator<Text> {

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
		catch (final RuntimeException re) {/* Ignore */}
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

	public boolean isEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	public void setEmptyStringAllowed(final boolean emptyStringAllowed) {
		this.emptyStringAllowed = emptyStringAllowed;
	}

}
