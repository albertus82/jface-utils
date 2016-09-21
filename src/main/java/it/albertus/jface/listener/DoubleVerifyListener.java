package it.albertus.jface.listener;

import it.albertus.util.Configured;

/** Accepts only double inputs and trims automatically. */
public class DoubleVerifyListener extends NumberVerifyListener {

	public DoubleVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public DoubleVerifyListener(final boolean allowNegatives) {
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
			Double.parseDouble(string);
			return true;
		}
		catch (final Exception e) {
			if (".".equals(string) || "e".equalsIgnoreCase(string) || (allowNegatives.getValue() && "-".equals(string))) {
				return true;
			}
			return false;
		}
	}

}
