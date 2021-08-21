package it.albertus.jface.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import it.albertus.util.ISupplier;
import it.albertus.util.logging.LoggerFactory;

abstract class AbstractDecimalVerifyListener<T extends Number> extends AbstractNumberVerifyListener<T> {

	private static final Logger log = LoggerFactory.getLogger(AbstractDecimalVerifyListener.class);

	protected AbstractDecimalVerifyListener(final boolean allowNegatives) {
		super(allowNegatives);
	}

	protected AbstractDecimalVerifyListener(final ISupplier<Boolean> allowNegatives) {
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
				if (".".equals(string) || "e".equalsIgnoreCase(string) || (allowNegatives.get() && "-".equals(string))) {
					return true;
				}
			}
		}
		return false;
	}

}
