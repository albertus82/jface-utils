package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class IntegerTextValidator extends NumberTextValidator<Integer> {

	public IntegerTextValidator(final Text text) {
		super(text);
	}

	public IntegerTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
	}

	public IntegerTextValidator(final Text text, final boolean emptyStringAllowed, final Integer minValidValue, final Integer maxValidValue) {
		super(text, emptyStringAllowed, minValidValue, maxValidValue);
	}

	@Override
	protected Integer toNumber(final String value) throws NumberFormatException {
		return Integer.valueOf(value);
	}

}
