package io.github.albertus82.jface.listener;

import java.math.BigInteger;

import io.github.albertus82.util.ISupplier;

/** Accepts only {@code BigInteger} inputs and trims automatically. */
public class BigIntegerVerifyListener extends AbstractIntegerVerifyListener<BigInteger> {

	public BigIntegerVerifyListener(final ISupplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public BigIntegerVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected BigInteger parseNumber(final String string) {
		return new BigInteger(string);
	}

}
