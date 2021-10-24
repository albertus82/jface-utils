package io.github.albertus82.jface.listener;

import io.github.albertus82.util.ISupplier;

/** Accepts only {@code Long} inputs and trims automatically. */
public class LongVerifyListener extends AbstractIntegerVerifyListener<Long> {

	public LongVerifyListener(final ISupplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public LongVerifyListener(boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Long parseNumber(final String string) {
		return Long.valueOf(string);
	}

}
