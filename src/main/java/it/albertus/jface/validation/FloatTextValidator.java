package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class FloatTextValidator extends NumberTextValidator<Float> {

	public FloatTextValidator(final Text text) {
		super(text);
	}

	public FloatTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
	}

	public FloatTextValidator(final Text text, final boolean emptyStringAllowed, final Float minValidValue, final Float maxValidValue) {
		super(text, emptyStringAllowed, minValidValue, maxValidValue);
	}

	@Override
	protected Float toNumber(final String value) throws NumberFormatException {
		return Float.valueOf(value);
	}

}
