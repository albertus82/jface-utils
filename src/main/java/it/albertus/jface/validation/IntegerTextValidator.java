package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class IntegerTextValidator extends NumberTextValidator<Integer> {

	public IntegerTextValidator(final Text text) {
		super(text);
	}

	public IntegerTextValidator(final Text text, final Integer minValidValue, final Integer maxValidValue, final boolean emptyStringAllowed) {
		super(text, minValidValue, maxValidValue, emptyStringAllowed);
	}

	@Override
	protected Integer toNumber(final String value) throws NumberFormatException {
		return Integer.valueOf(value);
	}

}
