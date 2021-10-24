package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class ByteTextValidator extends NumberTextValidator<Byte> {

	public ByteTextValidator(final Text text) {
		super(text);
	}

	public ByteTextValidator(final Text text, final boolean emptyStringAllowed) {
		super(text, emptyStringAllowed);
	}

	public ByteTextValidator(final Text text, final boolean emptyStringAllowed, final Byte minValidValue, final Byte maxValidValue) {
		super(text, emptyStringAllowed, minValidValue, maxValidValue);
	}

	@Override
	protected Byte toNumber(final String value) {
		return Byte.valueOf(value);
	}

}
