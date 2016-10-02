package it.albertus.jface.decoration;

import it.albertus.jface.validation.TextValidator;
import it.albertus.util.Localized;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class TextDecorationKeyListener extends KeyAdapter {

	protected final ControlDecoration controlDecoration;
	protected final TextValidator validator;
	protected final Localized message;

	public TextDecorationKeyListener(final ControlDecoration controlDecoration, final TextValidator validator, final Localized message) {
		this.controlDecoration = controlDecoration;
		this.validator = validator;
		this.message = message;
	}

	@Override
	public void keyReleased(final KeyEvent ke) {
		if (validator.isValid()) {
			controlDecoration.hide();
			validator.onValid();
		}
		else {
			controlDecoration.setDescriptionText(message.getString());
			controlDecoration.show();
			validator.onInvalid();
		}
	}

}
