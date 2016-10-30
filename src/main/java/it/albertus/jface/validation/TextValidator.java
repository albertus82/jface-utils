package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public abstract class TextValidator implements Validator {

	protected final Text text;

	public TextValidator(final Text text) {
		this.text = text;
	}

	public Text getText() {
		return text;
	}

}
