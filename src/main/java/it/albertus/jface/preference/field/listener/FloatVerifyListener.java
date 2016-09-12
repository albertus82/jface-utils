package it.albertus.jface.preference.field.listener;

/** Accepts only numeric inputs and trims automatically. */
public class FloatVerifyListener extends NumberVerifyListener {

	@Override
	protected boolean isNumeric(final String string) {
		try {
			Float.parseFloat(string);
			return true;
		}
		catch (final Exception e) {
			return false;
		}
	}

}
