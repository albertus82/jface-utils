package it.albertus.jface.listener;

import javax.annotation.Nullable;

import it.albertus.util.ISupplier;

abstract class AbstractIntegerVerifyListener<T extends Number> extends AbstractNumberVerifyListener<T> {

	public AbstractIntegerVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	public AbstractIntegerVerifyListener(final ISupplier<Boolean> allowNegatives) {
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
				if (Boolean.TRUE.equals(allowNegatives.get()) && "-".equals(string)) {
					return true;
				}
			}
		}
		return false;
	}

}
