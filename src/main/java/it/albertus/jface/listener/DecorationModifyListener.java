package it.albertus.jface.listener;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import it.albertus.jface.validation.Validator;
import it.albertus.util.Localized;

public class DecorationModifyListener implements ModifyListener, Listener {

	protected final ControlDecoration controlDecoration;
	protected final Validator validator;
	protected final Localized message;

	public DecorationModifyListener(final ControlDecoration controlDecoration, final Validator validator, final Localized message) {
		this.controlDecoration = controlDecoration;
		this.validator = validator;
		this.message = message;
	}

	@Override
	public void modifyText(final ModifyEvent me) {
		if (validator.isValid()) {
			controlDecoration.hide();
		}
		else {
			controlDecoration.setDescriptionText(message.getString());
			controlDecoration.show();
		}
	}

	@Override
	public void handleEvent(final Event event) {
		modifyText(null);
	}

}
