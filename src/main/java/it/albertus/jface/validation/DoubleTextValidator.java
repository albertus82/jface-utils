package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class DoubleTextValidator extends NumberTextValidator<Double> {

	public DoubleTextValidator(final Text text) {
		super(text);
	}

	public DoubleTextValidator(final Text text, final Double minValidValue, final Double maxValidValue, final boolean emptyStringAllowed) {
		super(text, minValidValue, maxValidValue, emptyStringAllowed);
	}

	@Override
	protected Double toNumber(final String value) throws NumberFormatException {
		return Double.valueOf(value);
	}

}
