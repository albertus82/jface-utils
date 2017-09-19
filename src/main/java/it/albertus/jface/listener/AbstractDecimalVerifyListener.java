package it.albertus.jface.listener;

import javax.annotation.Nullable;

import it.albertus.util.ISupplier;

abstract class AbstractDecimalVerifyListener<T extends Number> extends AbstractNumberVerifyListener<T> {

	public AbstractDecimalVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	public AbstractDecimalVerifyListener(final ISupplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected boolean isNumeric(@Nullable final String string) {
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
