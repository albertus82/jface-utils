package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class ShortTextValidator extends NumberTextValidator<Short> {

	public ShortTextValidator(final Text text) {
		super(text);
	}

	public ShortTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
	}

	public ShortTextValidator(final Text text, final boolean emptyStringAllowed, final Short minValidValue, final Short maxValidValue) {
		super(text, emptyStringAllowed, minValidValue, maxValidValue);
	}

	@Override
	protected Short toNumber(final String value) throws NumberFormatException {
		return Short.valueOf(value);
	}

}
