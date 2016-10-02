package it.albertus.jface.listener;

import it.albertus.util.Configured;

/** Accepts only {@code Integer} inputs and trims automatically. */
public class IntegerVerifyListener extends AbstractIntegerVerifyListener<Integer> {

	public IntegerVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public IntegerVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Integer parseNumber(final String string) throws NumberFormatException {
		return Integer.valueOf(string);
	}

}
