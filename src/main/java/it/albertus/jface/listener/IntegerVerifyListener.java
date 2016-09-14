package it.albertus.jface.listener;

import it.albertus.util.Configured;

/** Accepts only integer inputs and trims automatically. */
public class IntegerVerifyListener extends NumberVerifyListener {

	public IntegerVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public IntegerVerifyListener(final boolean allowNegatives) {
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
			Integer.parseInt(string);
			return true;
		}
		catch (final Exception e) {
			if (allowNegatives.getValue() && string.equals("-")) {
				return true;
			}
			return false;
		}
	}

}
