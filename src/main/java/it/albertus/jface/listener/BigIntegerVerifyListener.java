package it.albertus.jface.listener;

import it.albertus.util.Configured;

import java.math.BigInteger;

/** Accepts only double inputs and trims automatically. */
public class BigIntegerVerifyListener extends NumberVerifyListener {

	public BigIntegerVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public BigIntegerVerifyListener(final boolean allowNegatives) {
		this(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return allowNegatives;
			}
		});
	}

	@Override
	protected boolean isNumeric(final String string) {
		try {
			new BigInteger(string);
			return true;
		}
		catch (final Exception e) {
			if (allowNegatives.getValue() && "-".equals(string)) {
				return true;
			}
			return false;
		}
	}

}
