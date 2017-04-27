package it.albertus.jface.listener;

import it.albertus.util.Supplier;

import java.math.BigDecimal;

/** Accepts only {@code BigDecimal} inputs and trims automatically. */
public class BigDecimalVerifyListener extends AbstractDecimalVerifyListener<BigDecimal> {

	public BigDecimalVerifyListener(final Supplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public BigDecimalVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected BigDecimal parseNumber(final String string) throws NumberFormatException {
		return new BigDecimal(string);
	}

}
