package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Text;

public class ByteTextValidator extends NumberTextValidator<Byte> {

	public ByteTextValidator(final Text text) {
		super(text);
	}

	public ByteTextValidator(final Text text, final Byte minValidValue, final Byte maxValidValue, final boolean emptyStringAllowed) {
		super(text, minValidValue, maxValidValue, emptyStringAllowed);
	}

	@Override
	protected Byte toNumber(final String value) throws NumberFormatException {
		return Byte.valueOf(value);
	}

}
