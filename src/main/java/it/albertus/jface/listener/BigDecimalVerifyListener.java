package it.albertus.jface.listener;

import it.albertus.util.Configured;

import java.math.BigDecimal;

/** Accepts only double inputs and trims automatically. */
public class BigDecimalVerifyListener extends NumberVerifyListener {

	public BigDecimalVerifyListener(final Configured<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	public BigDecimalVerifyListener(final boolean allowNegatives) {
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
			new BigDecimal(string);
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
