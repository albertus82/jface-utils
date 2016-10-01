package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class FloatTextValidator extends NumberTextValidator<Float> {

	public FloatTextValidator(final Text text) {
		super(text);
	}

	public FloatTextValidator(final Text text, final Float minValidValue, final Float maxValidValue, final boolean emptyStringAllowed) {
		super(text, minValidValue, maxValidValue, emptyStringAllowed);
	}

	@Override
	protected Float toNumber(final String value) throws NumberFormatException {
		return Float.valueOf(value);
	}

}
