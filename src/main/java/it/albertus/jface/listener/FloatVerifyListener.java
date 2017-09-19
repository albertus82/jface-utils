package it.albertus.jface.listener;

import it.albertus.util.ISupplier;

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
