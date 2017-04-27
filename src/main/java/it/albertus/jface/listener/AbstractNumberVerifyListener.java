package it.albertus.jface.listener;

import it.albertus.util.Supplier;

import org.eclipse.swt.events.VerifyEvent;

/** Accepts only numeric inputs and trims automatically. */
abstract class AbstractNumberVerifyListener<T extends Number> extends TrimVerifyListener {

	protected final Supplier<Boolean> allowNegatives;

	protected AbstractNumberVerifyListener(final Supplier<Boolean> allowNegatives) {
		this.allowNegatives = allowNegatives;
	}

	protected AbstractNumberVerifyListener(final boolean allowNegatives) {
		this(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return allowNegatives;
			}
		});
	}

	@Override
	public void verifyText(final VerifyEvent ve) {
		super.verifyText(ve); // Trim
		if (ve.text.length() > 0 && !isNumeric(ve.text)) {
			ve.doit = false;
		}
	}

	protected abstract boolean isNumeric(String string);

	protected abstract T parseNumber(String string) throws NumberFormatException;

}
