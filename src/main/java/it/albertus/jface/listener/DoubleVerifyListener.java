package it.albertus.jface.listener;

import it.albertus.util.Supplier;

/** Accepts only {@code Double} inputs and trims automatically. */
public class DoubleVerifyListener extends AbstractDecimalVerifyListener<Double> {

	public DoubleVerifyListener(final Supplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public DoubleVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Double parseNumber(final String string) throws NumberFormatException {
		return Double.valueOf(string);
	}

}
