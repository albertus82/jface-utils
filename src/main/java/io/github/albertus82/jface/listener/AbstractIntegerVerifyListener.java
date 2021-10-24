package io.github.albertus82.jface.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import io.github.albertus82.util.ISupplier;
import io.github.albertus82.util.logging.LoggerFactory;

abstract class AbstractIntegerVerifyListener<T extends Number> extends AbstractNumberVerifyListener<T> {

	private static final Logger log = LoggerFactory.getLogger(AbstractIntegerVerifyListener.class);

	protected AbstractIntegerVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	protected AbstractIntegerVerifyListener(final ISupplier<Boolean> allowNegatives) {
		super(allowNegatives);
	}

	@Override
	protected boolean isNumeric(@Nullable final String string) {
		if (string != null) {
			try {
				parseNumber(string);
				return true;
			}
			catch (final NumberFormatException e) {
				log.log(Level.FINEST, "The value provided is not a valid representation of a number:", e);
				if (Boolean.TRUE.equals(allowNegatives.get()) && "-".equals(string)) {
					return true;
				}
			}
		}
		return false;
	}

}
