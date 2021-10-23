package io.github.albertus82.jface.listener;

import java.math.BigDecimal;

import io.github.albertus82.util.ISupplier;

/** Accepts only {@code BigDecimal} inputs and trims automatically. */
public class BigDecimalVerifyListener extends AbstractDecimalVerifyListener<BigDecimal> {

	public BigDecimalVerifyListener(final ISupplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public BigDecimalVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected BigDecimal parseNumber(final String string) {
		return new BigDecimal(string);
	}

}
