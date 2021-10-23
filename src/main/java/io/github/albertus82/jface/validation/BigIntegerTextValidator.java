package io.github.albertus82.jface.validation;

import java.math.BigInteger;

import org.eclipse.swt.widgets.Text;

public class BigIntegerTextValidator extends NumberTextValidator<BigInteger> {

	public BigIntegerTextValidator(final Text text) {
		super(text);
	}

	public BigIntegerTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
	}

	public BigIntegerTextValidator(final Text text, final boolean emptyStringAllowed, final BigInteger minValidValue, final BigInteger maxValidValue) {
		super(text, emptyStringAllowed, minValidValue, maxValidValue);
	}

	@Override
	protected BigInteger toNumber(final String value) {
		return new BigInteger(value);
	}

}
