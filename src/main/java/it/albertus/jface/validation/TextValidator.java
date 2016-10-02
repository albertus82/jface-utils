package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public abstract class TextValidator implements IValidator {

	protected final Text text;

	public TextValidator(final Text text) {
		this.text = text;
	}

	@Override
	public abstract boolean isValid();

	public void onValid() {}

	public void onInvalid() {}

	public Text getText() {
		return text;
	}

}
