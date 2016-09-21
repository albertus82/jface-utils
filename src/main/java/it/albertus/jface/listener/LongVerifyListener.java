package it.albertus.jface.listener;

import it.albertus.util.Configured;

/** Accepts only integer inputs and trims automatically. */
public class LongVerifyListener extends NumberVerifyListener {

	public LongVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public LongVerifyListener(final boolean allowNegatives) {
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
			Long.parseLong(string);
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
