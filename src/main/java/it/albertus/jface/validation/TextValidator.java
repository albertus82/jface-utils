package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public abstract class TextValidator {

	protected final Text text;

	public TextValidator(final Text text) {
		this.text = text;
	}

	public abstract boolean isValid();

	public void whenValid() {}

	public void whenInvalid() {}

	public Text getText() {
		return text;
	}

}
