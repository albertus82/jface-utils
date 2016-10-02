package it.albertus.jface.preference.field;

import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

abstract class AbstractNumberFieldEditor<T extends Number & Comparable<? extends Number>> extends EnhancedStringFieldEditor {

	private T minValidValue;
	private T maxValidValue;

	public AbstractNumberFieldEditor(final String name, final String labelText, final Composite parent) {
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
	protected boolean checkState() {
		final Text text = getTextControl();
		if (text == null) {
			return false;
		}
		if (isEmptyStringAllowed() && text.getText().isEmpty()) {
			clearErrorMessage();
			return true;
		}
		return doCheckState();
	}

	protected boolean checkValidRange(final Comparable<T> number) {
		if ((getMinValidValue() == null || number.compareTo(getMinValidValue()) >= 0) && (getMaxValidValue() == null || number.compareTo(getMaxValidValue()) <= 0)) {
			return true;
		}
		return false;
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

	public abstract T getNumberValue() throws NumberFormatException;

	protected abstract void updateTextLimit();

	protected abstract void updateErrorMessage();

}
