package it.albertus.jface.listener;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

/** Trims input text and converts its characters to lower case. */
public class LowercaseVerifyListener implements VerifyListener {

	@Override
	public void verifyText(final VerifyEvent ve) {
		ve.text = ve.text.trim().toLowerCase();
	}

}
