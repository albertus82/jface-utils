package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class ShortTextValidator extends NumberTextValidator<Short> {

	public ShortTextValidator(final Text text) {
		super(text);
	}

	public ShortTextValidator(final Text text, final Short minValidValue, final Short maxValidValue, final boolean emptyStringAllowed) {
		super(text, minValidValue, maxValidValue, emptyStringAllowed);
	}

	@Override
	protected Short toNumber(final String value) throws NumberFormatException {
		return Short.valueOf(value);
	}

}
