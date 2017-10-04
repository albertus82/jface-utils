package it.albertus.jface.validation;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Text;

public class BigDecimalTextValidator extends NumberTextValidator<BigDecimal> {

	public BigDecimalTextValidator(final Text text) {
		super(text);
	}

	public BigDecimalTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
	}

	public BigDecimalTextValidator(final Text text, final boolean emptyStringAllowed, final BigDecimal minValidValue, final BigDecimal maxValidValue) {
		super(text, emptyStringAllowed, minValidValue, maxValidValue);
	}

	@Override
	protected BigDecimal toNumber(final String value) {
		return new BigDecimal(value);
	}

}
