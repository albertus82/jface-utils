package it.albertus.jface.validation;

import org.eclipse.swt.widgets.Control;

public abstract class ControlValidator<T extends Control> implements Validator {

	protected final T control;

	public ControlValidator(final T control) {
		this.control = control;
	}

	public T getControl() {
		return control;
	}

}
