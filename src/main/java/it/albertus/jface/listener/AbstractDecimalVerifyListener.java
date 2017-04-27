package it.albertus.jface.listener;

import it.albertus.util.Supplier;

abstract class AbstractDecimalVerifyListener<T extends Number> extends AbstractNumberVerifyListener<T> {

	public AbstractDecimalVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	public AbstractDecimalVerifyListener(final Supplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected boolean isNumeric(final String string) {
		if (string != null) {
			try {
				parseNumber(string);
				return true;
			}
			catch (final NumberFormatException nfe) {
				if (".".equals(string) || "e".equalsIgnoreCase(string) || (allowNegatives.get() && "-".equals(string))) {
					return true;
				}
			}
		}
		return false;
	}

}
