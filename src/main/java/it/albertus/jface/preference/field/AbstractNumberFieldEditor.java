package it.albertus.jface.preference.field;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Composite;

import it.albertus.util.logging.LoggerFactory;

abstract class AbstractNumberFieldEditor<T extends Number & Comparable<? extends Number>> extends EnhancedStringFieldEditor {

	private static final Logger log = LoggerFactory.getLogger(AbstractNumberFieldEditor.class);

	private T minValidValue;
	private T maxValidValue;

	protected AbstractNumberFieldEditor(final String name, final String labelText, final Composite parent) {
		init(name, labelText);
		setEmptyStringAllowed(false);
		setTextLimit(getDefaultTextLimit());
		createControl(parent);
		updateErrorMessage();
		addDecoration();
	}

	protected int getDefaultTextLimit() {
		return Preferences.MAX_VALUE_LENGTH;
	}

	@Override
	protected boolean doCheckState() {
		try {
			return checkValidRange(getNumberValue());
		}
		catch (final NumberFormatException e) {
			log.log(Level.FINEST, "The value provided is not a valid representation of a number:", e);
		}
		return false;
	}

	protected boolean checkValidRange(final Comparable<T> number) {
		return (getMinValidValue() == null || number.compareTo(getMinValidValue()) >= 0) && (getMaxValidValue() == null || number.compareTo(getMaxValidValue()) <= 0);
	}

	public void setValidRange(final T min, final T max) {
		setMinValidValue(min);
		setMaxValidValue(max);
	}

	public T getMinValidValue() {
		return minValidValue;
	}

	public void setMinValidValue(final T min) {
		this.minValidValue = min;
		updateErrorMessage();
		updateTextLimit();
	}

	public T getMaxValidValue() {
		return maxValidValue;
	}

	public void setMaxValidValue(final T max) {
		this.maxValidValue = max;
		updateErrorMessage();
		updateTextLimit();
	}

	public abstract Comparable<T> getNumberValue();

	protected abstract void updateTextLimit();

	protected abstract void updateErrorMessage();

}
