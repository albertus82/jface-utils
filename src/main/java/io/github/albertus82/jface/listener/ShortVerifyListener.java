package io.github.albertus82.jface.listener;

import io.github.albertus82.util.ISupplier;

/** Accepts only {@code Short} inputs and trims automatically. */
public class ShortVerifyListener extends AbstractIntegerVerifyListener<Short> {

	public ShortVerifyListener(final ISupplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public ShortVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Short parseNumber(final String string) {
		return Short.valueOf(string);
	}

}
