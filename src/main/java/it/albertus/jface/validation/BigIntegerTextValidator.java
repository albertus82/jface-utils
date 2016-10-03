package it.albertus.jface.validation;

import java.math.BigInteger;

import org.eclipse.swt.widgets.Text;

public class BigIntegerTextValidator extends NumberTextValidator<BigInteger> {

	public BigIntegerTextValidator(final Text text) {
		super(text);
	}

	public BigIntegerTextValidator(final Text text, final BigInteger minValidValue, final BigInteger maxValidValue, final boolean emptyStringAllowed) {
		super(text, minValidValue, maxValidValue, emptyStringAllowed);
	}

	@Override
	protected BigInteger toNumber(final String value) throws NumberFormatException {
		return new BigInteger(value);
	}

}
