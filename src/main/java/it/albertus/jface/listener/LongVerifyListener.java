package it.albertus.jface.listener;

import it.albertus.util.Supplier;

/** Accepts only {@code Long} inputs and trims automatically. */
public class LongVerifyListener extends AbstractIntegerVerifyListener<Long> {

	public LongVerifyListener(final Supplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public LongVerifyListener(boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Long parseNumber(final String string) throws NumberFormatException {
		return Long.valueOf(string);
	}

}
