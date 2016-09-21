package it.albertus.jface.listener;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

/** Convert input text to upper case. */
public class UpperCaseVerifyListener implements VerifyListener {

	@Override
	public void verifyText(final VerifyEvent ve) {
		ve.text = ve.text.toUpperCase();
	}

}
