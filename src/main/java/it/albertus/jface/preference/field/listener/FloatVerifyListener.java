package it.albertus.jface.preference.field.listener;

import it.albertus.util.Configured;

/** Accepts only numeric inputs and trims automatically. */
public class FloatVerifyListener extends NumberVerifyListener {

	protected FloatVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected boolean isNumeric(final String string) {
		try {
			Float.parseFloat(string);
			return true;
		}
		catch (final Exception e) {
			if (".".equals(string) || (allowNegatives.getValue() && "-".equals(string))) {
				return true;
			}
			return false;
		}
	}

}
