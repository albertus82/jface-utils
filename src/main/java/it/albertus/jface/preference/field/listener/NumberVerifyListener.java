package it.albertus.jface.preference.field.listener;

import org.eclipse.swt.events.VerifyEvent;

/** Accepts only numeric inputs and trims automatically. */
public abstract class NumberVerifyListener extends TrimVerifyListener {

	@Override
	public void verifyText(final VerifyEvent ve) {
		super.verifyText(ve); // Trim
		if (ve.text.length() > 0 && !isNumeric(ve.text)) {
			ve.doit = false;
		}
	}

	protected abstract boolean isNumeric(final String string);

}
