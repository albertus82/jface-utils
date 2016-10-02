package it.albertus.jface.listener;

import it.albertus.util.Configured;

/** Accepts only {@code Short} inputs and trims automatically. */
public class ByteVerifyListener extends AbstractIntegerVerifyListener<Byte> {

	public ByteVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public ByteVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected Byte parseNumber(final String string) throws NumberFormatException {
		return Byte.valueOf(string);
	}

}
