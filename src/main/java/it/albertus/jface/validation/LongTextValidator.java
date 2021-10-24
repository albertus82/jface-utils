package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class LongTextValidator extends NumberTextValidator<Long> {

	public LongTextValidator(final Text text) {
		super(text);
	}

	public LongTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
	}

	public LongTextValidator(final Text text, final boolean emptyStringAllowed, final Long minValidValue, final Long maxValidValue) {
		super(text, emptyStringAllowed, minValidValue, maxValidValue);
	}

	@Override
	protected Long toNumber(final String value) {
		return Long.valueOf(value);
	}

}
