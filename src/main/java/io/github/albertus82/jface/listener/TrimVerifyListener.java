package io.github.albertus82.jface.listener;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

/** Trims input text. */
public class TrimVerifyListener implements VerifyListener {

	@Override
	public void verifyText(final VerifyEvent ve) {
		ve.text = ve.text.trim();
	}

}
