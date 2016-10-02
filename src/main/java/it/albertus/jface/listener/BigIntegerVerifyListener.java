package it.albertus.jface.listener;

import it.albertus.util.Configured;

import java.math.BigInteger;

/** Accepts only {@code BigInteger} inputs and trims automatically. */
public class BigIntegerVerifyListener extends AbstractIntegerVerifyListener<BigInteger> {

	public BigIntegerVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public BigIntegerVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected BigInteger parseNumber(final String string) throws NumberFormatException {
		return new BigInteger(string);
	}

}
