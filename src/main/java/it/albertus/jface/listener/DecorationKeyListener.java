package it.albertus.jface.listener;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

import it.albertus.jface.validation.Validator;
import it.albertus.util.Localized;

public class DecorationKeyListener extends KeyAdapter {

	protected final ControlDecoration controlDecoration;
	protected final Validator validator;
	protected final Localized message;

	public DecorationKeyListener(final ControlDecoration controlDecoration, final Validator validator, final Localized message) {
		this.controlDecoration = controlDecoration;
		this.validator = validator;
		this.message = message;
	}

	@Override
	public void keyReleased(final KeyEvent ke) {
		if (validator.isValid()) {
			controlDecoration.hide();
		}
		else {
			controlDecoration.setDescriptionText(message.getString());
			controlDecoration.show();
		}
	}

}
