package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class LongTextValidator extends NumberTextValidator<Long> {

	public LongTextValidator(final Text text) {
		super(text);
	}

	public LongTextValidator(final Text text, final Long minValidValue, final Long maxValidValue, final boolean emptyStringAllowed) {
		super(text, minValidValue, maxValidValue, emptyStringAllowed);
	}

	@Override
	protected Long toNumber(final String value) throws NumberFormatException {
		return Long.valueOf(value);
	}

}
