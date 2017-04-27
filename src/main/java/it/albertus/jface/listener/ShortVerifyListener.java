package it.albertus.jface.listener;

import it.albertus.util.Supplier;

/** Accepts only {@code Short} inputs and trims automatically. */
public class ShortVerifyListener extends AbstractIntegerVerifyListener<Short> {

	public ShortVerifyListener(final Supplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public ShortVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Short parseNumber(final String string) throws NumberFormatException {
		return Short.valueOf(string);
	}

}
