package io.github.albertus82.jface.listener;

import io.github.albertus82.util.ISupplier;

/** Accepts only {@code Double} inputs and trims automatically. */
public class DoubleVerifyListener extends AbstractDecimalVerifyListener<Double> {

	public DoubleVerifyListener(final ISupplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public DoubleVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Double parseNumber(final String string) {
		return Double.valueOf(string);
	}

}
