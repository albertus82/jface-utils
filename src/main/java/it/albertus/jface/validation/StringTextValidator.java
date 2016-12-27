package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class StringTextValidator extends ControlValidator<Text> {

	public static final boolean EMPTY_STRING_ALLOWED = true;

	private boolean emptyStringAllowed = EMPTY_STRING_ALLOWED;

	public StringTextValidator(final Text text) {
		super(text);
	}

	public StringTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text);
		this.emptyStringAllowed = emptyStringAllowed;
	}

	@Override
	public boolean isValid() {
		return isEmptyStringAllowed() || !getControl().getText().isEmpty();
	}

	public boolean isEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	public void setEmptyStringAllowed(final boolean emptyStringAllowed) {
		this.emptyStringAllowed = emptyStringAllowed;
	}

}
