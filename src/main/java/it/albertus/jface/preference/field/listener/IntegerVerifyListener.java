package it.albertus.jface.preference.field.listener;

import it.albertus.util.Configured;

/** Accepts only numeric inputs and trims automatically. */
public class IntegerVerifyListener extends NumberVerifyListener {

	public IntegerVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
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
