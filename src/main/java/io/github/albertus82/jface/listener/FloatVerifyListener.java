package io.github.albertus82.jface.listener;

import io.github.albertus82.util.ISupplier;

/** Accepts only {@code Float} inputs and trims automatically. */
public class FloatVerifyListener extends AbstractDecimalVerifyListener<Float> {

	public FloatVerifyListener(final ISupplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public FloatVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Float parseNumber(final String string) {
		return Float.valueOf(string);
	}

}
