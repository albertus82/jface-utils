package io.github.albertus82.jface.validation;

import org.eclipse.swt.widgets.Control;

public abstract class ControlValidator<T extends Control> implements Validator {

	protected final T control;

	protected ControlValidator(final T control) {
		this.control = control;
	}

	public T getControl() {
		return control;
	}

}
