package io.github.albertus82.jface.listener;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

/** Convert input text to lower case. */
public class LowerCaseVerifyListener implements VerifyListener {

	@Override
	public void verifyText(final VerifyEvent ve) {
		ve.text = ve.text.toLowerCase();
	}

}
