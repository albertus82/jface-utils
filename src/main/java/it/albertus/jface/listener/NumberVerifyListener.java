package it.albertus.jface.listener;

import it.albertus.util.Configured;

import org.eclipse.swt.events.VerifyEvent;

/** Accepts only numeric inputs and trims automatically. */
public abstract class NumberVerifyListener extends TrimVerifyListener {

	protected final Configured<Boolean> allowNegatives;

	protected NumberVerifyListener(final Configured<Boolean> allowNegatives) {
		this.allowNegatives = allowNegatives;
	}

	@Override
	public void verifyText(final VerifyEvent ve) {
		super.verifyText(ve); // Trim
		if (ve.text.length() > 0 && !isNumeric(ve.text)) {
			ve.doit = false;
		}
	}

	protected abstract boolean isNumeric(String string);

}
